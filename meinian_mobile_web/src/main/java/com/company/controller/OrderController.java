package com.company.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.company.constant.MessageConstant;
import com.company.constant.RedisMessageConstant;
import com.company.entity.Result;
import com.company.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    OrderService orderService;
    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/submit.do")

    public Result submit(@RequestBody Map map){
        //获取客户端的手机号
        String telephone= map.get("telephone").toString();
        //获取客户端验证码
        String validateCode=map.get("validateCode").toString();
        //获取redis中的验证码
        String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //验证码比较
        if(code==null||!code.equals(validateCode)){
            return  new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }else {
            //下订单
            Result result=null;
            try {
                result= orderService.add(map);
            } catch (Exception e) {
                e.printStackTrace();
                return  new Result(false, MessageConstant.ORDER_FAIL);
            }
            return result;
        }

    }
    @RequestMapping("/findById.do")
    public Result findById(Integer id) {
        Map map = null;
        try {
            map = orderService.findById4Detail(id);
            System.out.println(map);
        } catch (Exception e) {
            //查询预约信息失败
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,map);
    }
}