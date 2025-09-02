package com.cow.horse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.config.AuthAccess;
import com.cow.horse.entity.ProductParams;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.cow.horse.service.IProductParamsService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/product-params")
public class ProductParamsController {

    @Resource
    private IProductParamsService productParamsService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody ProductParams productParams) {
        return Result.success(productParamsService.saveOrUpdate(productParams));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(productParamsService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(productParamsService.removeByIds(ids));
    }

    @AuthAccess
    @GetMapping("/{id}")
    public Result findOneParams(@PathVariable Integer id) {
        return productParamsService.findOneParams(id);
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<ProductParams> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(productParamsService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

