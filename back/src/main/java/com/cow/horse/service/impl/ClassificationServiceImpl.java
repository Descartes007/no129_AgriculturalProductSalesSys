package com.cow.horse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cow.horse.commcn.Result;
import com.cow.horse.controller.dto.HomeMenuResultDTO;
import com.cow.horse.entity.Classification;
import com.cow.horse.entity.Product;
import com.cow.horse.mapper.ClassificationMapper;
import com.cow.horse.mapper.ProductMapper;
import com.cow.horse.service.IClassificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClassificationServiceImpl extends ServiceImpl<ClassificationMapper, Classification> implements IClassificationService {

    @Autowired
    ClassificationMapper classificationMapper;

    @Autowired
    ProductMapper productMapper;

    @Override
    public List<HomeMenuResultDTO> getMenus() {
        return classificationMapper.getMenus();
    }

    @Override
    public Result classification(Integer id) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("classification_id", id);
        queryWrapper.eq("is_delete", 0);
        return Result.success(productMapper.selectList(queryWrapper));
    }
}
