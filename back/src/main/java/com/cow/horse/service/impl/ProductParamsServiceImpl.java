package com.cow.horse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cow.horse.commcn.Result;
import com.cow.horse.config.AuthAccess;
import com.cow.horse.entity.ProductParams;
import com.cow.horse.mapper.ProductParamsMapper;
import com.cow.horse.service.IProductParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductParamsServiceImpl extends ServiceImpl<ProductParamsMapper, ProductParams> implements IProductParamsService {

    @Autowired
    ProductParamsMapper productParamsMapper;

    @AuthAccess
    @Override
    public Result findOneParams(Integer id) {
        QueryWrapper<ProductParams> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", id);
        return Result.success(productParamsMapper.selectOne(queryWrapper));
    }
}
