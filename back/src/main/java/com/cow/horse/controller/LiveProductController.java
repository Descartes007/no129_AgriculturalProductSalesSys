package com.cow.horse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.entity.LiveProduct;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.cow.horse.service.ILiveProductService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/live-product")
public class LiveProductController {

    @Resource
    private ILiveProductService liveProductService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody LiveProduct liveProduct) {
        return Result.success(liveProductService.saveOrUpdate(liveProduct));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(liveProductService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(liveProductService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(liveProductService.list());
    }

    @GetMapping("/{id}")
    public Result findAll(@PathVariable Integer id) {
        List<LiveProduct> list = liveProductService.findAll(id);
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<LiveProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(liveProductService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

