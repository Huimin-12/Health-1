package cn.controller;

import cn.constant.MessageConstant;
import cn.constant.RedisMessageConstant;
import cn.domain.Order;
import cn.entity.Result;
import cn.service.OrderService;
import cn.utils.SMSUtils;
import cn.utils.ValidateCodeUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;
    /**
     * 体检预约发送手机验证码
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        //生成6位数的手机验证码
       Integer code = ValidateCodeUtils.generateValidateCode(6);
       //给手机发送短信,第一个参数是发送什么类型的短信，第二个参数是电话号码，第三个参数是随机验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            //验证码发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //把生成的验证码存入redis当中，设置它的存活时间为5分钟
        jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_ORDER,5*60,code.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        //获取手机号
        String telephone = (String) map.get("telephone");
        //从redis当中获取已经存储的验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //获取用户输入的验证码
        String validateCode = (String) map.get("validateCode");
        Result result = null;
        //比较两个验证码是否相同
        if (codeInRedis!=null&&validateCode!=null&&codeInRedis.equals(validateCode)){

            try {
                map.put("orderType", Order.ORDERTYPE_WEIXIN);
                //调用体检预约服务
                result = orderService.order(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //表示预约成功，发送短信给客户
            if (result.isFlag()){
                try {
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone, (String) map.get("orderDate"));
                } catch (ClientException e) {
                    e.printStackTrace();
                    return result;
                }
            }
        }else {
            //手机号校验失败
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
        return result;
    }
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        //生成6位数的手机验证码
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        //给手机发送短信,第一个参数是发送什么类型的短信，第二个参数是电话号码，第三个参数是随机验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            //验证码发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //把生成的验证码存入redis当中，设置它的存活时间为5分钟
        jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,30*60,code.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
