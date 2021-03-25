package com.company.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.company.constant.MessageConstant;
import com.company.entity.PageResult;
import com.company.entity.QueryPageBean;
import com.company.entity.Result;
import com.company.pojo.TravelItem;
import com.company.service.TravelItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController  //作用 : 包含两个注解 @ResponseBody  @Controller
@RequestMapping("/travelItem")
public class TravelItemController {
    @Reference
    TravelItemService travelItemService;
    @RequestMapping("/add.do")
    //添加权限控制访问
    @PreAuthorize("hasAuthority('TRAVELITEM_ADD')")//权限校验
    public Result add(@RequestBody TravelItem travelItem){
        try {
            travelItemService.add(travelItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_TRAVELITEM_SUCCESS);
    }
    //分页查询 查询结果返回pageResult
    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult= travelItemService.findPage(queryPageBean);
        return pageResult;
    }
    @RequestMapping("/delete.do")
    @PreAuthorize("hasAuthority('abc')")//权限校验
    public Result delete(Integer id){
        try {
            travelItemService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_TRAVELITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_TRAVELITEM_SUCCESS);
    }
    @RequestMapping("/findById.do")
    @PreAuthorize("hasAuthority('TRAVELITEM_QUERY')")//权限校验
    public Result findById(Integer id){
        TravelItem travelItem= travelItemService.findById(id);
        return new Result(true,MessageConstant.QUERY_TRAVELITEM_SUCCESS,travelItem);
    }
    @RequestMapping("/edit.do")
    @PreAuthorize("hasAuthority('TRAVELITEM_EDIT')")
    public Result edit(@RequestBody TravelItem travelItem){
        try {
            travelItemService.edit(travelItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_TRAVELITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_TRAVELITEM_SUCCESS);
    }
    @RequestMapping("/findAll.do")
    //查询所有的自由行
    public Result findAll(){
        List<TravelItem> travelItemList= travelItemService.findAll();
        return new Result(true,MessageConstant.QUERY_TRAVELITEM_SUCCESS,travelItemList);
    }
}
