package com.cow.horse.service;

import com.cow.horse.entity.Evaluate;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IEvaluateService extends IService<Evaluate> {

    Evaluate findOneByProductId(Integer id);
}
