package com.cow.horse.controller;

import com.cow.horse.commcn.Result;
import com.cow.horse.config.AuthAccess;
import com.cow.horse.entity.Product;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.cow.horse.service.IProductService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private IProductService productService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Product product) {
        Integer ClassificationId = productService.seleclassId(product.getClassificationName());
        product.setClassificationId(ClassificationId);
        product.setCity("哈尔滨市");
        productService.saveOrUpdate(product);
        return Result.success(product.getId());
    }

    @DeleteMapping("/{id}")
    public Result deleteProduct(@PathVariable Integer id) {
        return productService.deleteProduct(id);
    }

    @PostMapping("/del/batch")
    public Result deleteProducts(@RequestBody List<Integer> ids) {
        return Result.success(productService.deleteProducts(ids));
    }

    @AuthAccess
    @GetMapping
    public Result findAll() {
        return Result.success(productService.list());
    }

    @AuthAccess
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(productService.getById(id));
    }

    @AuthAccess
    @GetMapping("/page")
    public Result findPage(
            @RequestParam String name,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {
        return Result.success(productService.findPage(pageNum, pageSize, name));
    }

    @AuthAccess
    @GetMapping("/lovely/{city}")
    public Result findLovely(@PathVariable String city) {
        return productService.findLovely(city);
    }


    @AuthAccess
    @GetMapping("/recommend/{city}")
    public Result findRecommend(@PathVariable String city) {
        return productService.findRecommend(city);
    }


    @AuthAccess
    @GetMapping("/pages")
    public Result findPages(
            @RequestParam String city,
            @RequestParam String name,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {
        return Result.success(productService.findPages(pageNum, pageSize, name, city));
    }
}

