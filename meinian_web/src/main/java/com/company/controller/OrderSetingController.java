package com.company.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.company.constant.MessageConstant;
import com.company.entity.Result;
import com.company.pojo.OrderSetting;
import com.company.service.OrderSettingService;
import com.company.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSetingController {
    //上床文件

    @Reference
    OrderSettingService orderSettingService;

    @RequestMapping("/upload.do")
    public Result upload(MultipartFile excelFile) throws IOException {
        try {
            // 使用poi工具类解析excel文件，读取里面的内容
            List<String[]> lists = POIUtils.readExcel(excelFile);
            // 把List<String[]> 数据转换成 List<OrderSetting>数据
            List<OrderSetting> orderSettings = new ArrayList<>();
            //  迭代里面的每一行数据，进行封装到集合里面
            for (String[] str : lists) {
                // 获取到一行里面，每个表格数据，进行封装
                OrderSetting orderSetting = new OrderSetting(new Date(str[0]),Integer.parseInt(str[1]));
                orderSettings.add(orderSetting);
            }
            // 调用业务进行保存
            orderSettingService.add(orderSettings);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }

    }

    @RequestMapping("/getOrderSettingByMonth.do")
    public Result getOrderSettingByMoth(String date){
        try{
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            //获取预约设置数据成功
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            //获取预约设置数据失败
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }

    }


    @RequestMapping("/editNumberByDate.do")
    public Result editNumberByDate(@RequestBody  OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
        return  new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
    }


}
