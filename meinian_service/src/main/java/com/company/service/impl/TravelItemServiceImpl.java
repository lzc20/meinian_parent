package com.company.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.company.dao.TravelItemDao;
import com.company.entity.PageResult;
import com.company.entity.QueryPageBean;
import com.company.pojo.TravelItem;
import com.company.service.TravelItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = TravelItemService.class)
@Transactional
public class TravelItemServiceImpl implements TravelItemService {
    @Autowired
    TravelItemDao travelItemDao;
    public void add(TravelItem travelItem) {
        travelItemDao.add(travelItem);
    }

    public PageResult findPage(QueryPageBean queryPageBean) {
        //1. 使用PageHelper 进行分页 select * from t_travelItem where queryString=? limit ?,?
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());//limit?,?
        //2. 查询条件 queryString
        Page<TravelItem> page= travelItemDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    public void delete(Integer id) {
        //查询被删除的自由行有没有关联跟团游
        long count=  travelItemDao.selectTravelgroup_TravelitemByTravelIemId(id);
        if(count>0){
            throw new RuntimeException("自由行不能被删除");
        }else {
            travelItemDao.delete(id);
        }
    }

    public TravelItem findById(Integer id) {
        return  travelItemDao.findById(id);
    }

    public void edit(TravelItem travelItem) {
        travelItemDao.edit(travelItem);
    }

    public List<TravelItem> findAll() {
        return travelItemDao.findAll();
    }
}
