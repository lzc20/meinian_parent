package com.company.controller;

import com.company.constant.MessageConstant;
import com.company.constant.RedisMessageConstant;
import com.company.entity.Result;
import com.company.utils.SMSUtils;
import com.company.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
//短息验证码控制器
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    JedisPool jedisPool;

    //预约是发送手机验证码
    @RequestMapping("/send4Order.do")

    public Result send$Order(String telephone){
        //生成4位数字验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        try{
            SMSUtils.sendShortMessage(telephone,code.toString());
        }catch (Exception  e){
            e.printStackTrace();
            //验证码发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);

        }
        System.out.println("发送的验证码为："+code);
        //将生成的验证码缓存到redis
        jedisPool.getResource().setex(
                telephone+ RedisMessageConstant.SENDTYPE_ORDER,10*6,code.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);


    }
    @RequestMapping("/send4Login.do")
    public Result send4Login(String telephone) throws Exception {
        //生成手机对应的验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);

        //发送给手机
        SMSUtils.sendShortMessage(telephone, code.toString());
        //在redis 中保存验证码
        String jedisCode = jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 5 * 60, code.toString());

        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}