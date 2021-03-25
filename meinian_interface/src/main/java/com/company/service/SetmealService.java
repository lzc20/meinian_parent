package com.company.service;

import com.company.entity.PageResult;
import com.company.entity.QueryPageBean;
import com.company.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    public void add(Integer[] travelgroupIds,Setmeal setmeal);

    PageResult findPage(QueryPageBean queryPageBean);

    //移动端的查询
    List<Setmeal> getSetmeal();

    Setmeal findById(Integer id);

    Setmeal getSetmealById(Integer id);

    List<Map> querySetmeal();
}
