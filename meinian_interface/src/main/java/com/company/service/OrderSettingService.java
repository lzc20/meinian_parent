package com.company.service;


import com.company.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    List<Map> getOrderSettingByMonth(String date);

    void add(List<OrderSetting> orderSettings);

    void editNumberByDate(OrderSetting orderSetting);
}
