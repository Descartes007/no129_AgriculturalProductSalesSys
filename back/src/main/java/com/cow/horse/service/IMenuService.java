package com.cow.horse.service;

import com.cow.horse.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface IMenuService extends IService<Menu> {

    List<Menu> findMenues(String name);
}
