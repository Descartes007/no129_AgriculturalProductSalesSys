package com.cow.horse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cow.horse.entity.Menu;
import com.cow.horse.mapper.MenuMapper;
import com.cow.horse.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<Menu> findMenues(String name) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        //查询所有数据
        List<Menu> list = list(queryWrapper);
        //找出一级菜单 pid=null
        //  stream().filter流操作  Java8操作  收集(collect)  Collectors.toList() 列表  javase内容
        List<Menu> parenNodes = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        //找二级菜单
        for (Menu menu : parenNodes) {
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parenNodes;
    }
}
