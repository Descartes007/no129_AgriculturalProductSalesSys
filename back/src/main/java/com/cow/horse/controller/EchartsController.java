package com.cow.horse.controller;

import com.cow.horse.commcn.Result;
import com.cow.horse.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private IOrdersService ordersService;

    @GetMapping("/members")
    public Result members() {
        return Result.success(ordersService.members());
    }

}
