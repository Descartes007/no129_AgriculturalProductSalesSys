package com.cow.horse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.entity.User;
import com.cow.horse.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.cow.horse.service.IReceivingService;
import com.cow.horse.entity.Receiving;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/receiving")
public class ReceivingController {

    @Resource
    private IReceivingService receivingService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Receiving receiving) {
        return Result.success(receivingService.saveOrUpdateReceiving(receiving));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(receivingService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(receivingService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(
                receivingService.findAll());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(receivingService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        User currentUser = TokenUtils.getCurrentUser();
        QueryWrapper<Receiving> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", currentUser.getId());
        queryWrapper.orderByDesc("id");
        return Result.success(receivingService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

