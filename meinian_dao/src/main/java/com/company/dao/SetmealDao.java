package com.company.dao;

import com.company.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    public void add(Setmeal setmeal);

    void insert_setmeal_travelgroup(Map map);

    Page<Setmeal> findPage(String queryString);

    List<Setmeal> getSetmeal();

    //查询套餐信息包括跟团游，跟团游包括自由行
    Setmeal findById(Integer id);

    Setmeal getSetmealById(Integer id);

    List<Map> querySetmeal();
}
