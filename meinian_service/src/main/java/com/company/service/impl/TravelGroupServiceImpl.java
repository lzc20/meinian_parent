package com.company.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.company.dao.TravelGroupDao;
import com.company.entity.PageResult;
import com.company.pojo.TravelGroup;
import com.company.service.TravelGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service(interfaceClass = TravelGroupService.class)
@Transactional
public class TravelGroupServiceImpl implements TravelGroupService {
    @Autowired
    TravelGroupDao travelGroupDao;
    @Override
    public void add(Integer[] travelItemIds, TravelGroup travelGroup) {
        //1.添加跟团游数据
        travelGroupDao.add(travelGroup);
        //2. 添加跟团游和自由行的关系数据到关系表
        insert_travelGroup_travelItem(travelGroup.getId(),travelItemIds);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 使用分页插件PageHelper，设置当前页，每页最多显示的记录数
        PageHelper.startPage(currentPage,pageSize);
        // 响应分页插件的Page对象
        Page<TravelGroup> page = travelGroupDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());

    }

    @Override
    public List<Integer> findTravelItemIdByTravelgroupId(Integer id) {
        return travelGroupDao.findTravelItemIdByTravelgroupId(id);
    }

    @Override
    public TravelGroup findById(Integer id) {
        return travelGroupDao.findById(id);
    }

    @Override
    public void edit(Integer[] travelItemIds, TravelGroup travelGroup) {
// 1：修改跟团游的基本信息
        travelGroupDao.edit(travelGroup);
        /**
         * 2：修改跟团游和自由行的中间表（先删除，再创建）
         * 之前的数据删除
         * 再新增页面选中的数据
         */
        // 删除之前中间表的数据
        travelGroupDao.deleteTravelGroupAndTravelItemByTravelGroupId(travelGroup.getId());
        // 再新增页面选中的数据
        insert_travelGroup_travelItem(travelGroup.getId(),travelItemIds);

    }

    @Override
    public List<TravelGroup> findAll() {
        return travelGroupDao.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        // 在删除跟团行之前，先判断自由行的id，在中间表中是否存在数据
        long count =  travelGroupDao.findCountByTravelGroupId(id);
        // 中间表如果有数据，不要往后面执行，直接抛出异常
        // 如果非要删除也可以：delete from t_travelgroup_travelitem where travelitem_id = 1
        if (count > 0){
            throw new RuntimeException("不允许删除");
        }
        // 使用自由行的id进行删除
        travelGroupDao.deleteById(id);

    }


    public void insert_travelGroup_travelItem(Integer travelGroupId,Integer[] travelItemIds){
        for (Integer travelItemId : travelItemIds) {
            Map map=new HashMap();
            map.put("travelGroupId",travelGroupId);
            map.put("travelItemId",travelItemId);
            travelGroupDao.insert_travelGroup_travelItem(map);
        }
    }
}
