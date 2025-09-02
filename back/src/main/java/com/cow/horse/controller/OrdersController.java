package com.cow.horse.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.entity.Orders;
import com.cow.horse.entity.OrdersProduct;
import com.cow.horse.entity.Product;
import com.cow.horse.entity.Examine;
import com.cow.horse.service.IOrdersProductService;
import com.cow.horse.service.IOrdersService;
import com.cow.horse.service.IProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Resource
    private IOrdersService ordersService;

    @Resource
    private IOrdersProductService ordersProductService;


    @Resource
    private IProductService productService;


    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Orders orders) {
        return ordersService.saveOrUpdateOrders(orders);
    }

    //直接购买
    @PostMapping("/buy")
    public Result buy(@RequestBody Orders orders) {
        return ordersService.buy(orders);
    }

    @DeleteMapping("/{id}")
    public Result logicalDeletion(@PathVariable Integer id) {

        return ordersService.logicalDeletion(id);
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(ordersService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(ordersService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(ordersService.getById(id));
    }

    @PostMapping("/cancel/{id}")
    public Result cancelOrder(@PathVariable Integer id) {

        return Result.success(ordersService.cancelOrder(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String stateName,
                           @RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        Page<Orders> OrderList = ordersService.finPage(pageNum, pageSize, name, stateName);
        return Result.success(OrderList);
    }

    //支付
    @PostMapping("/payment")
    public Result payment(@RequestBody Orders orders) {
        productState(orders);
        LocalDateTime time = LocalDateTime.now();
        orders.setPayTime(time.toString());
        boolean b = ordersService.updateById(orders);
        if (b) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    //发货
    @PostMapping("/deliverGoods")
    public Result deliverGoods(@RequestBody Orders orders) {
        String now = DateUtil.now();
        orders.setDeliveryTime(now);
        productState(orders);
        boolean b = ordersService.updateById(orders);
        if (b) {
            return Result.success();
        } else {
            return Result.error();
        }
    }


    @GetMapping("/getProductById/{id}")
    public Result getProductById(@PathVariable Integer id) {
        QueryWrapper<OrdersProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", id);
        List<OrdersProduct> list = ordersProductService.list(queryWrapper);
        ArrayList<Product> productArrayList = new ArrayList<>();
        for (OrdersProduct ordersProduct : list) {
            Integer productId = ordersProduct.getProductId();
            Product product = productService.getById(productId);
            product.setNum(ordersProduct.getNum());
            productArrayList.add(product);
        }
        return Result.success(productArrayList);
    }

    @GetMapping("/stateName/{stateName}")
    public Result findStateName(@PathVariable String stateName) {
        return ordersService.findStateName(stateName);
    }


    //取消订单 审核
    @PostMapping("/examine")
    public Result Examine(@RequestBody Examine examine) {
        return ordersService.Examine(examine);
    }


    private Orders productState(Orders orders) {
        orders.setState(orders.getState() + 1);
        return orders;
    }


}

