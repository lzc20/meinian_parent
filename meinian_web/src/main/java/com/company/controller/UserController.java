package com.company.controller;

import com.company.constant.MessageConstant;
import com.company.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getUsername.do")
    public Result getUsername(){
        //获取当前登录的用户名
        User user=null;
        try{
            //返回给框架里的用户的用户名，所以在这里可以往框架里要username
            /**
             * securityContextHolder权限框架的上下文
             * 在这个容器里获取，已经获取过权限的用户名
             * getPrincipal--框架封装的用户名
             */
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            System.out.println("获取的用户名是："+user.getUsername());
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }

        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user);

    }
}
