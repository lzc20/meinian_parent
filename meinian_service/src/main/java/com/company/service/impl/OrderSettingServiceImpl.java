package com.company.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.company.dao.OrderSettingDao;
import com.company.pojo.OrderSetting;
import com.company.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    OrderSettingDao orderSettingDao;
    @Override
    public void add(List<OrderSetting> orderSettingList) {
        //遍历List<OrderSetting>
        for (OrderSetting orderSetting : orderSettingList) {
            // 判断当前的日期之前是否已经被设置过预约日期，使用当前时间作为条件查询数量
            long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
            // 如果设置过预约日期，更新number数量
            if (count>0){
                orderSettingDao.editNumberByOrderDate(orderSetting);
            }else {
                // 如果没有设置过预约日期，执行保存
                orderSettingDao.add(orderSetting);
            }

        }
    }


    public List<Map> getOrderSettingByMonth(String date) {
        // 2.查询当前月份的预约设置
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(date);
        List<Map> data = new ArrayList<>();
        // 3.将List<OrderSetting>，组织成List<Map>
        for (OrderSetting orderSetting : list) {
            Map orderSettingMap = new HashMap();
            orderSettingMap.put("date", orderSetting.getOrderDate().getDate());//获得日期（几号）
            orderSettingMap.put("number", orderSetting.getNumber());//可预约人数
            orderSettingMap.put("reservations", orderSetting.getReservations());//已预约人数
            data.add(orderSettingMap);
        }
        return data;
    }




    public void editNumberByDate(OrderSetting orderSetting) {
        System.out.println("OrderSetting==========="+orderSetting);
        OrderSetting orderSettingDB=
                orderSettingDao.findOrderSettingObjectByOrderDate(orderSetting.getOrderDate());
        if(orderSettingDB==null){//该日期还没有被设置
            orderSettingDao.add(orderSetting);
        }else{
            if(orderSettingDB.getReservations()>orderSetting.getNumber()){//如果已预约的人数超过了可预约的人数
                throw new RuntimeException("不能进行设置");
            }else{
                orderSettingDao.editNumberByOrderDate(orderSetting);
            }
        }
    }
}
