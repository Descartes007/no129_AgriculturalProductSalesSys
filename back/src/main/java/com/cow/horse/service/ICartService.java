package com.cow.horse.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cow.horse.commcn.Result;
import com.cow.horse.entity.Cart;

import java.util.List;

public interface ICartService extends IService<Cart> {

    Page<Cart> findPage(String name, Integer pageNum, Integer pageSize);

    void updateNum(Integer id, Integer num);

    Result saveOrUpdateCart(Cart cart);

    List<Cart> findAll();
}
