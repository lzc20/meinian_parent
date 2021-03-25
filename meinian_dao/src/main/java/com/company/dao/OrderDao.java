package com.company.dao;

import com.company.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    Order getOrder(Map param);

    void add(Order order);

    Map findById4Detail(Integer id);

    int getTodayOrderNumber(String today);

    int getTodayVisitsNumber(String today);

    int getThisWeekAndMonthOrderNumber(Map<String, Object> paramWeek);

    int getThisWeekAndMonthVisitsNumber(Map<String, Object> paramWeekVisit);

    List<Map<String, Object>> findHotSetmeal();
}
