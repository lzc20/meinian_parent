package com.company.dao;

import com.company.pojo.TravelGroup;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface TravelGroupDao {

    void add(TravelGroup travelGroup);

    void insert_travelGroup_travelItem(Map map);

    Page<TravelGroup> findPage(String queryString);

    List<Integer> findTravelItemIdByTravelgroupId(Integer id);

    TravelGroup findById(Integer id);

    void edit(TravelGroup travelGroup);

    void deleteTravelGroupAndTravelItemByTravelGroupId(Integer id);



    List<TravelGroup> findAll();

    long findCountByTravelGroupId(Integer id);

    void deleteById(Integer id);

    List<TravelGroup> findTravegroupsBySetmealId(Integer setMealid);
}
