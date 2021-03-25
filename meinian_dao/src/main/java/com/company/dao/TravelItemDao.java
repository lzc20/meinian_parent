package com.company.dao;

import com.company.pojo.TravelItem;
import com.github.pagehelper.Page;

import java.util.List;

public interface TravelItemDao {

    public  void add(TravelItem travelItem);

    Page<TravelItem> findPage(String queryString);

    long selectTravelgroup_TravelitemByTravelIemId(Integer id);

    void delete(Integer id);

    TravelItem findById(Integer id);

    void edit(TravelItem travelItem);

    List<TravelItem> findAll();


    List<TravelItem> getTraveItemsByTravelGroupId(Integer travelGroupId);
}
