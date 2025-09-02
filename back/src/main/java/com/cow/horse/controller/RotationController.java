package com.cow.horse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.config.AuthAccess;
import com.cow.horse.entity.Rotation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.cow.horse.service.IRotationService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/rotation")
public class RotationController {

    @Resource
    private IRotationService rotationService;


    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Rotation rotation) {
        return Result.success(rotationService.saveOrUpdate(rotation));
    }

    //逻辑删
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        QueryWrapper<Rotation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Rotation rotation = rotationService.getOne(queryWrapper);
        rotation.setIsDelete(true);
        rotationService.updateById(rotation);
        return Result.success();
    }


    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {

        QueryWrapper<Rotation> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<Rotation> rotationList = rotationService.list(queryWrapper);
        for (Rotation rotation : rotationList) {
            rotation.setIsDelete(true);
            rotationService.updateById(rotation);
        }
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(rotationService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Rotation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", false);
        queryWrapper.orderByDesc("id");
        return Result.success(rotationService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @AuthAccess
    @GetMapping("/list")
    public Result rotationAll() {
        QueryWrapper<Rotation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enable", true);
        queryWrapper.eq("is_delete", 0);
        queryWrapper.select("url");
        List<Map<String, Object>> list = rotationService.listMaps(queryWrapper);
        return Result.success(list);
    }
}

