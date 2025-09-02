package com.cow.horse.controller;

import com.cow.horse.commcn.Constants;
import com.cow.horse.commcn.Result;
import com.cow.horse.entity.Dict;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.cow.horse.service.IDictService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/dict")
public class DictController {

    @Resource
    private IDictService dictService;

    @GetMapping("/icons")
    public Result getIcons() {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.DICT_TYPE_ICON);
        return Result.success(dictService.list(queryWrapper));
    }

    @GetMapping("/reason")
    public Result getReason() {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.DICT_TYPE_REASON);
        return Result.success(dictService.list(queryWrapper));
    }

}

