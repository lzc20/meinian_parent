package com.company.service;

import com.company.entity.PageResult;
import com.company.pojo.TravelGroup;

import java.util.List;

public interface TravelGroupService {
    void add(Integer[] travelItemIds, TravelGroup travelGroup);
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    List<Integer> findTravelItemIdByTravelgroupId(Integer id);

    TravelGroup findById(Integer id);

    void edit(Integer[] travelItemIds, TravelGroup travelGroup);

    List<TravelGroup> findAll();

    void deleteById(Integer id);
}
