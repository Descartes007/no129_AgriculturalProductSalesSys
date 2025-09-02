package com.cow.horse.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.entity.Product;
import com.cow.horse.entity.Collection;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface ICollectionService extends IService<Collection> {

    Page<Collection> findPage(Integer pageNum, Integer pageSize, String name);

    List<Product> findAllByUserId(String username);

    Result saveCollection(Collection collection);

    Result delete(Integer productId, String username);

    Result findPageReception(Integer pageNum, Integer pageSize, String productName, String username);

    Result selectCollection(String productId, String username);
}
