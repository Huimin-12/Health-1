package cn.controller;

import cn.constant.MessageConstant;
import cn.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getUsername")
    public Result getUsername(){
        //当spring security 完成认证后，会将当前用户信息存储到框架提供的应用上下文
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);
        if (user!=null){
            //获取到的user不为空的话，就取出当前用户名
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        }
        //返回错误信息
        return new Result(false,MessageConstant.GET_USERNAME_FAIL);
    }
}
