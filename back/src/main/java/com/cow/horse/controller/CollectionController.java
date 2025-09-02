package com.cow.horse.controller;

import com.cow.horse.commcn.Result;
import com.cow.horse.config.AuthAccess;
import com.cow.horse.entity.Product;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.cow.horse.service.ICollectionService;
import com.cow.horse.entity.Collection;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Resource
    private ICollectionService collectionService;

    // 新增或者更新
    @AuthAccess
    @PostMapping
    public Result save(@RequestBody Collection collection) {
        return collectionService.saveCollection(collection);
    }

    @DeleteMapping("/{productId}/{username}")
    public Result delete(@PathVariable Integer productId, @PathVariable String username) {
        return collectionService.delete(productId, username);
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(collectionService.removeByIds(ids));
    }

    @AuthAccess
    @GetMapping("/{username}")
    public Result findAllByUserId(@PathVariable String username) {
        List<Product> productList = collectionService.findAllByUserId(username);
        return Result.success(productList);
    }

    @AuthAccess
    @GetMapping("/{productId}/{username}")
    public Result selectCollection(@PathVariable String productId, @PathVariable String username) {
        return collectionService.selectCollection(productId, username);
    }

    @AuthAccess
    @GetMapping("/pageReception")
    public Result findPageReception(
            @RequestParam(defaultValue = "") String productName,
            @RequestParam String username,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {
        return collectionService.findPageReception(pageNum, pageSize, productName, username);
    }


    @GetMapping("/page")
    public Result findPage(
            @RequestParam String name,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {
        return Result.success(collectionService.findPage(pageNum, pageSize, name));
    }
}

