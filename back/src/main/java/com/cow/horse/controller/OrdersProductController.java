package com.cow.horse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.entity.OrdersProduct;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.cow.horse.service.IOrdersProductService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/orders-product")
public class OrdersProductController {

    @Resource
    private IOrdersProductService ordersProductService;

//    // 新增或者更新
//    @PostMapping
//    public Result save(@RequestBody OrdersProduct ordersProduct) {
//        return Result.success(ordersProductService.saveOrUpdate(ordersProduct));
//    }

    @PostMapping
    public Result update(@RequestBody OrdersProduct ordersProduct) {
        QueryWrapper<OrdersProduct> queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", ordersProduct.getProductId());
        queryWrapper.eq("order_id", ordersProduct.getOrderId());
        OrdersProduct one = ordersProductService.getOne(queryWrapper);
        one.setNum(ordersProduct.getNum());
        return Result.success(ordersProductService.updateById(one));
    }

    @PostMapping("/del")
    public Result delete(@RequestBody OrdersProduct ordersProduct) {
        QueryWrapper<OrdersProduct> queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", ordersProduct.getProductId());
        queryWrapper.eq("order_id", ordersProduct.getOrderId());
        OrdersProduct one = ordersProductService.getOne(queryWrapper);
        return Result.success(ordersProductService.removeById(one));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(ordersProductService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(ordersProductService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(ordersProductService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<OrdersProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(ordersProductService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

