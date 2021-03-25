package com.company.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.company.constant.MessageConstant;
import com.company.dao.MemberDao;
import com.company.dao.OrderDao;
import com.company.dao.OrderSettingDao;
import com.company.entity.Result;
import com.company.pojo.Member;
import com.company.pojo.Order;
import com.company.pojo.OrderSetting;
import com.company.service.OrderService;
import com.company.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderSettingDao orderSettingDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    OrderDao orderDao;

    public Result add(Map map) throws Exception {
        /* 1. 判断当前的日期是否可以预约(根据orderDate查询t_ordersetting, 能查询出来可以预约;查询不出来,不能预约)*/
        //获取预约的日期
        Date date = DateUtils.parseString2Date(map.get("orderDate").toString());
        OrderSetting orderSetting = orderSettingDao.findOrderSettingObjectByOrderDate(date);
        if (orderSetting == null) {//预约的日期没有设置活动
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        } else {
            /* 2. 判断当前日期是否预约已满(判断reservations（已经预约人数）是否等于number（最多预约人数）)*/
            int number = orderSetting.getNumber();//可预约人数
            int reservations = orderSetting.getReservations();//已经预约的人数
            if(reservations>=number){
                return new Result(false, MessageConstant.ORDER_FULL);
            }else{


      /*  3. 判断是否是会员(根据手机号码查询t_member)
                - 如果是会员(能够查询出来), 防止重复预约(根据member_id,orderDate,setmeal_id查询t_order)
                - 如果不是会员(不能够查询出来),自动注册为会员(直接向t_member插入一条记录)
*/
                //获取手机号
                String telephone= map.get("telephone").toString();
                Member member= memberDao.getMemberByTelephone(telephone);
                if(member!=null){//是会员
                    //- 如果是会员(能够查询出来), 防止重复预约(根据member_id,orderDate,setmeal_id查询t_order)
                    Map param=new HashMap();
                    param.put("member_id",member.getId());
                    param.put("orderDate",date);
                    param.put("setmeal_id",map.get("setmealId"));
                    Order order= orderDao.getOrder(param);
                    if(order!=null){
                        return new Result(false, MessageConstant.HAS_ORDERED);
                    }
                }else{//不是会员
                    //- 如果不是会员(不能够查询出来),自动注册为会员(直接向t_member插入一条记录)
                    member=new Member();
                    member.setName(map.get("name").toString());
                    member.setSex(map.get("sex").toString());
                    member.setIdCard(map.get("idCard").toString());
                    member.setRegTime(new Date());
                    member.setPhoneNumber(telephone);
                    memberDao.add(member);
                }

                  /*  4.进行预约

                - t_ordersetting表里面预约的人数reservations+1*/
                orderSetting.setReservations(orderSetting.getReservations()+1);
                orderSettingDao.editReservations(orderSetting);
                //- 向t_order表插入一条记录
                Order order=new Order();
                order.setOrderStatus(Order.ORDERSTATUS_NO);
                order.setOrderType("微信预约");
                order.setMemberId(member.getId());
                order.setSetmealId(Integer.parseInt(map.get("setmealId").toString()));
                order.setOrderDate(date);
                orderDao.add(order);
                return new Result(true,MessageConstant.ORDER_SUCCESS,order);
            }

        }


    }

    @Override
    //根据id查询预约信息包括旅游人信息，套餐信息
    public Map findById4Detail(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);//多张表的数据过来用map
        if (map!=null){
            //处理日期格式
            Date orderDate=(Date)map.get("orderDate");
            map.put("ordrDate",DateUtils.parseDate2String(orderDate));
            return map;
        }
        return map;
    }
}




