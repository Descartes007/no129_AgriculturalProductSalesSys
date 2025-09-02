package com.cow.horse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.config.AuthAccess;
import com.cow.horse.controller.dto.HomeMenuResultDTO;
import com.cow.horse.entity.Classification;
import com.cow.horse.entity.Product;
import com.cow.horse.service.IProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.cow.horse.service.IClassificationService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/classification")
public class ClassificationController {

    @Resource
    private IClassificationService classificationService;

    @Resource
    private IProductService productService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Classification classification) {
        return Result.success(classificationService.saveOrUpdate(classification));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("classification_id", id);
        List<Product> list = productService.list(queryWrapper);
        for (Product product : list) {
            product.setIsDelete(true);
            productService.updateById(product);
        }
        return Result.success(classificationService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(classificationService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(classificationService.list());
    }

    @AuthAccess
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(classificationService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Classification> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByDesc("id");
        return Result.success(classificationService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @AuthAccess
    @GetMapping("/menus")
    public Result getMenus() {
        List<HomeMenuResultDTO> homeMenuResultDTOList = classificationService.getMenus();
        return Result.success(homeMenuResultDTOList);
    }

    @AuthAccess
    @GetMapping("/class/{id}")
    public Result classification(@PathVariable Integer id) {
        return classificationService.classification(id);
    }

}

