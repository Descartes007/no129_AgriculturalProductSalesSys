package com.cow.horse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.config.AuthAccess;
import com.cow.horse.entity.UserVisitLog;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.cow.horse.service.IUserVisitLogService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user-visit-log")
public class UserVisitLogController {

    @Resource
    private IUserVisitLogService userVisitLogService;

    // 新增或者更新
    @AuthAccess
    @PostMapping
    public Result save(@RequestBody UserVisitLog userVisitLog) {

        return userVisitLogService.saveUserVisitLog(userVisitLog);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userVisitLogService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(userVisitLogService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(userVisitLogService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userVisitLogService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<UserVisitLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(userVisitLogService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

