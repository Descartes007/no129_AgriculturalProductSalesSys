package com.cow.horse.controller;


import com.cow.horse.commcn.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.cow.horse.service.IExamineService;
import com.cow.horse.entity.Examine;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/examine")
public class ExamineController {

    @Resource
    private IExamineService examineService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Examine examine) {
        return Result.success(examineService.saveOrUpdate(examine));
    }

    @PostMapping("inspect")
    public Result inspect(@RequestBody Examine examine) {

        return examineService.inspect(examine);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(examineService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(examineService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(examineService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(examineService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        return examineService.findPage(pageNum, pageSize);
    }
}

