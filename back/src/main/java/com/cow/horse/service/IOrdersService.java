package com.cow.horse.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.controller.dto.AliPay;
import com.cow.horse.entity.Orders;
import com.cow.horse.entity.Examine;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IOrdersService extends IService<Orders> {

    Page<Orders> finPage(Integer pageNum, Integer pageSize, String name, String stateName);

    Result saveOrUpdateOrders(Orders orders);

    Result Examine(Examine examine);

    Result logicalDeletion(Integer id);

    Result findStateName(String stateName);

    Integer payment(String tradeNo, String gmtPayment, String alipayTradeNo);

    Integer updatePayState(String traceNo);



    Boolean cancelOrder(Integer id);

    Result buy(Orders orders);

    Result members();
}
