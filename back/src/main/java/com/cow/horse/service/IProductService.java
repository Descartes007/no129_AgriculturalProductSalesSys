package com.cow.horse.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface IProductService extends IService<Product> {

    Page<Product> findPage(Integer pageNum, Integer pageSize, String name);

    Integer seleclassId(String classificationName);

    Result findLovely(String city);

    Result deleteProduct(Integer id);

    Boolean deleteProducts(List<Integer> ids);

    Result findRecommend(String city);

    Page<Product> findPages(Integer pageNum, Integer pageSize, String name, String city);
}
