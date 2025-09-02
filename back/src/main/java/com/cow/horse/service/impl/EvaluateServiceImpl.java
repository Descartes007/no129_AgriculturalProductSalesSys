package com.cow.horse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cow.horse.config.AuthAccess;
import com.cow.horse.entity.Evaluate;
import com.cow.horse.mapper.EvaluateMapper;
import com.cow.horse.service.IEvaluateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EvaluateServiceImpl extends ServiceImpl<EvaluateMapper, Evaluate> implements IEvaluateService {

    @Autowired
    EvaluateMapper evaluateMapper;

    @AuthAccess
    @Override
    public Evaluate findOneByProductId(Integer id) {
        QueryWrapper<Evaluate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", id);
        Evaluate evaluate = evaluateMapper.selectOne(queryWrapper);
        return evaluate;
    }
}
