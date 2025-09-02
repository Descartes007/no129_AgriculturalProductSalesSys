package com.cow.horse.service;

import com.cow.horse.commcn.Result;
import com.cow.horse.entity.ProductParams;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IProductParamsService extends IService<ProductParams> {

    Result findOneParams(Integer id);
}
