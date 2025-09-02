package com.cow.horse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.config.AuthAccess;
import com.cow.horse.entity.Evaluate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.cow.horse.service.IEvaluateService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/evaluate")
public class EvaluateController {

    @Resource
    private IEvaluateService evaluateService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Evaluate evaluate) {
        return Result.success(evaluateService.saveOrUpdate(evaluate));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(evaluateService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(evaluateService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(evaluateService.list());
    }

    @AuthAccess
    @GetMapping("/{id}")
    public Result findOneByProductId(@PathVariable Integer id) {
        return Result.success(evaluateService.findOneByProductId(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Evaluate> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(evaluateService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

