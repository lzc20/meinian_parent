package com.company.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.company.constant.MessageConstant;
import com.company.entity.Result;
import com.company.pojo.Setmeal;
import com.company.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealContoller {
    @Reference
    SetmealService setmealService;

    //移动端的控制器（查询 所有的控制器）
    @RequestMapping("/getSetmeal.do")
    public Result getSetmeal(){
        List<Setmeal> setmealList= setmealService.getSetmeal();
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmealList);
    }

    @RequestMapping("/findById.do")
    public Result findById(Integer id){

        Setmeal setmeal = setmealService.findById(id);

        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

    @RequestMapping("/getSetmealById.do")
    public Result getSetmealById(Integer id){
        System.out.println("传过来的id：id="+id);
        Setmeal setmeal = setmealService.getSetmealById(id);

        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);

    }

}
