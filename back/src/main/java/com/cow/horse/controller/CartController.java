package com.cow.horse.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.entity.Cart;
import com.cow.horse.service.ICartService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/cart")
public class CartController {

    @Resource
    private ICartService cartService;

    // 新增或者更新
    @PostMapping
    public Result saveOrUpdateCart(@RequestBody Cart cart) {
        return Result.success(cartService.saveOrUpdateCart(cart));
    }

    @PostMapping("/num/{id}/{num}")
    public Result updateNum(@PathVariable Integer id, @PathVariable Integer num) {
        cartService.updateNum(id, num);
        return Result.success();
    }


    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(cartService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(cartService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        cartService.findAll();
        return Result.success(cartService.findAll());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(cartService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        Page<Cart> productList = cartService.findPage(name, pageNum, pageSize);
        return Result.success(productList);
    }


}

