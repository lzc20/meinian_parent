package com.company.service;

import com.company.entity.Result;

import java.util.Map;

public interface OrderService {
        Result add(Map map) throws Exception;

    Map findById4Detail(Integer id) throws Exception;
}
