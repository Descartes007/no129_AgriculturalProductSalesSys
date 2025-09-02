package com.cow.horse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.config.AuthAccess;
import com.cow.horse.entity.Article;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.cow.horse.service.IArticleService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private IArticleService articleService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Article article) {
        return Result.success(articleService.saveOrUpdate(article));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(articleService.removeById(id));
    }


    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(articleService.removeByIds(ids));
    }

    @AuthAccess
    @GetMapping
    public Result findAll() {
        return articleService.findAll();
    }

    @AuthAccess
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(articleService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(articleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

