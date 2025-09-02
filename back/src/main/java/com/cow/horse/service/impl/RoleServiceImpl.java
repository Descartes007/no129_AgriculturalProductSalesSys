package com.cow.horse.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cow.horse.entity.Role;
import com.cow.horse.entity.RoleMenu;
import com.cow.horse.entity.Menu;
import com.cow.horse.mapper.RoleMapper;
import com.cow.horse.mapper.RoleMenuMapper;
import com.cow.horse.service.IMenuService;
import com.cow.horse.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    //先删后增
    //事务注解 防止一步出错 回滚
    @Transactional
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        //先删除当前角色id所有的绑定关系
        List<Integer> menuIdsCopy = CollUtil.newCopyOnWriteArrayList(menuIds);
        roleMenuMapper.delete(queryWrapper);
        //再把前端传过来的菜单id数组绑定到当前的这个角色上去
        for (Integer menuId : menuIds) {
            Menu menu = menuService.getById(menuId);
            //一级菜单
            if (menu.getPid() != null && !menuIdsCopy.contains(menu.getPid())) {
                //那我们就得补上父级id

                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setMenuId(menu.getPid());
                roleMenu.setRoleId(roleId);
                roleMenuMapper.insert(roleMenu);
                menuIdsCopy.add(menu.getPid());
            }


            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.getRoleMenu(roleId);
    }
}
