package com.company.dao;

import com.company.pojo.OrderSetting;

import java.util.Date;
import java.util.List;

public interface OrderSettingDao {

    List<OrderSetting> getOrderSettingByMonth(String date);

    long findCountByOrderDate(Date orderDate);

    void editNumberByOrderDate(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);


    OrderSetting findOrderSettingObjectByOrderDate(Date orderDate);


    void editReservations(OrderSetting orderSetting);
}
