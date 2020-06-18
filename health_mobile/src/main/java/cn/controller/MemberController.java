package cn.controller;

import cn.constant.MessageConstant;
import cn.constant.RedisMessageConstant;
import cn.domain.Member;
import cn.entity.Result;
import cn.service.MemberService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;
    @RequestMapping("/login")
    public Result check(@RequestBody Map map, HttpServletResponse response){
        //获取手机号
        String telephone = (String) map.get("telephone");
        //获取用户输入的验证码和存在redis当中的验证码
        String validateCode = (String) map.get("validateCode");
        String codeInRedis = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_LOGIN);
//        1、校验用户输入的短信验证码是否正确，如果验证码错误则登录失败
        if (validateCode==null||codeInRedis==null||!validateCode.equals(codeInRedis)){
            //验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }else {
//        2、如果验证码正确，则判断当前用户是否为会员，如果不是会员则自动完成会员注册
            Member member = memberService.findByTelephone(telephone);
            if (member==null){
                //该用户还不是会员，需要自动注册成为会员
                member = new Member();
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                //自动注册成为会员
                memberService.add(member);
            }
//        3、向客户端写入Cookie，内容为用户手机号
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*60*24*30);//cookie存活时间30天
            //把cookie添加到响应体当中
            response.addCookie(cookie);
//        4、将会员信息保存到Redis，使用手机号作为key，保存时长为30分钟
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone, 60 * 30, json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }
    }
}
