package com.cow.horse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.config.AuthAccess;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.cow.horse.service.ICommentService;
import com.cow.horse.entity.Comment;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private ICommentService commentService;

    // 新增或者更新
    @PostMapping
    public Result saveOrUpdateByComment(@RequestBody Comment comment) {

        return Result.success(commentService.saveOrUpdateByComment(comment));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(commentService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(commentService.removeByIds(ids));
    }

    @AuthAccess
    @GetMapping
    public Result findAll() {
        return Result.success(commentService.list());
    }

    @AuthAccess
    @GetMapping("/tree/{productId}")
    public Result findTree(@PathVariable Integer productId) {
        return commentService.findTree(productId);
    }


    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(commentService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(commentService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

