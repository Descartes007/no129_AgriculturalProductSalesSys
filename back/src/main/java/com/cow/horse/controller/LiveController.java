package com.cow.horse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.cow.horse.service.ILiveService;
import com.cow.horse.entity.Live;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/live")
public class LiveController {

    @Resource
    private ILiveService liveService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Live live) {
        if (live.getId() == null) {
            live.setUserUrl(TokenUtils.getCurrentUser().getAvatarUrl());
        }
        return Result.success(liveService.saveOrUpdate(live));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(liveService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(liveService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(liveService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(liveService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String name
    ) {
        QueryWrapper<Live> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        return Result.success(liveService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

