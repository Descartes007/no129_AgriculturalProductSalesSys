package com.cow.horse.service.impl;

import com.cow.horse.entity.OrdersProduct;
import com.cow.horse.mapper.OrdersProductMapper;
import com.cow.horse.service.IOrdersProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class OrdersProductServiceImpl extends ServiceImpl<OrdersProductMapper, OrdersProduct> implements IOrdersProductService {

}
