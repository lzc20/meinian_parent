package com.company.service;

import com.company.entity.PageResult;
import com.company.entity.QueryPageBean;
import com.company.pojo.TravelItem;

import java.util.List;

public interface TravelItemService {
    public void add(TravelItem travelItem);

    PageResult findPage(QueryPageBean queryPageBean);

    void delete(Integer id);

    TravelItem findById(Integer id);

    void edit(TravelItem travelItem);

    List<TravelItem> findAll();
}
