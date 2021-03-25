package com.company.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.company.constant.MessageConstant;
import com.company.entity.PageResult;
import com.company.entity.QueryPageBean;
import com.company.entity.Result;
import com.company.pojo.TravelGroup;
import com.company.service.TravelGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travelGroup")
public class TravelGroupController {

    @Reference
    TravelGroupService travelGroupService;
    @RequestMapping("/add.do")
    public Result add(Integer[] travelItemIds, @RequestBody TravelGroup travelGroup){
        System.out.println("============================================");
        System.out.println(travelItemIds);
        System.out.println(travelGroup);
        System.out.println("Service====="+travelGroupService);

        try {
            travelGroupService.add(travelItemIds,travelGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
    }

    // 传递当前页，每页显示的记录数，查询条件
    // 响应PageResult，封装总记录数，结果集
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult =  travelGroupService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    // 使用自由行id，查询跟团游和自由行的中间表，获取自由行的集合，存放id的值
    @RequestMapping("/findTravelItemIdByTravelgroupId")
    public List<Integer> findTravelItemIdByTravelgroupId(Integer id){
        List<Integer> list =  travelGroupService.findTravelItemIdByTravelgroupId(id);
        return list;
    }


    // 使用id查询跟团游，进行表单回显
    @RequestMapping("/findById")
    public Result findById(Integer id){
        TravelGroup travelGroup =  travelGroupService.findById(id);
        return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,travelGroup);
    }

    // 编辑跟团游（返回 public Result(boolean flag, String message)）
    @RequestMapping("/edit")
    public Result edit(Integer[] travelItemIds,@RequestBody TravelGroup travelGroup ){
        travelGroupService.edit(travelItemIds,travelGroup);
        return new Result(true,MessageConstant.EDIT_TRAVELGROUP_SUCCESS);
    }
    //查询所有
    @RequestMapping("/findAll.do")
    public Result findAll(){
        //查询所有的跟团游
        List<TravelGroup> travelGroupList = travelGroupService.findAll();
        if (travelGroupList != null && travelGroupList.size()>0){
            Result result = new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,travelGroupList);
            return result;
        }
        return new Result(false,MessageConstant.QUERY_TRAVELGROUP_FAIL);
    }
    //删除跟团游
    @RequestMapping("/delete.do")
    public Result delete(Integer id){

        try {
            travelGroupService.deleteById(id);

        }catch (RuntimeException e){
            // 运行时异常，表示自由行和跟团游的关联表中存在数据
            return new Result(false,MessageConstant.DELETE_TRAVELGROUP_FAIL);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_TRAVELGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_TRAVELGROUP_SUCCESS);

    }



}
