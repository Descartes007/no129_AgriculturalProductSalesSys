package com.cow.horse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.config.AuthAccess;
import com.cow.horse.entity.ProductImg;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.cow.horse.service.IProductImgService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/product-img")
public class ProductImgController {


    @Resource
    private IProductImgService productImgService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody ProductImg productImg) {
        return Result.success(productImgService.saveOrUpdate(productImg));
    }


    @PostMapping("/{productId}")
    public Result saveImg(@PathVariable Integer productId, @RequestBody List<String> urlList) {
        return productImgService.saveImg(productId, urlList);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(productImgService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(productImgService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(productImgService.list());
    }

    @AuthAccess
    @GetMapping("/{id}")
    public Result findImgList(@PathVariable Integer id) {
        return Result.success(productImgService.findImgList(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<ProductImg> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(productImgService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

