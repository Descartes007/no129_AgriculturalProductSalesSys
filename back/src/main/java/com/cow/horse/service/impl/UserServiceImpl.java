package com.cow.horse.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cow.horse.commcn.Constants;
import com.cow.horse.commcn.Result;
import com.cow.horse.controller.dto.UserDTO;
import com.cow.horse.controller.dto.UserPasswordDTO;
import com.cow.horse.entity.HomeVO;
import com.cow.horse.entity.User;
import com.cow.horse.exception.ServiceException;
import com.cow.horse.utils.TokenUtils;
import com.cow.horse.entity.Menu;
import com.cow.horse.mapper.OrdersMapper;
import com.cow.horse.mapper.RoleMapper;
import com.cow.horse.mapper.RoleMenuMapper;
import com.cow.horse.mapper.UserMapper;
import com.cow.horse.service.IMenuService;
import com.cow.horse.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private static final Log LOG = Log.get();

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public UserDTO login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User user;
        try {
            user = getOne(queryWrapper);

        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        //复制bean类
        if (user != null) {
            BeanUtil.copyProperties(user, userDTO, true);
            //设置token
            String token = TokenUtils.genToken(user.getId().toString(), user.getPassword());
            userDTO.setToken(token);

            //获取登录用户角色唯一标识
            String flag = user.getRole(); //ROLE_ADMIN
            //设置用户菜单列表
            userDTO.setMenus(getRoleMenus(flag));
            return userDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public Result register(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        User user;
        try {
            user = getOne(queryWrapper);

        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        if (user == null) {
            //重新new一下 防止copy报错
            user = new User();
            BeanUtil.copyProperties(userDTO, user, true);
            user.setRole("ROLE_USER");
            user.setAvatarUrl("http://localhost:9090/file/头像.jpg");
            save(user);
        } else {
            throw new ServiceException(Constants.CODE_600, "已存在该用户");
        }

        return Result.success();
    }

    @Override
    public Result getUserByName(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user;
        try {
            user = getOne(queryWrapper);
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return Result.success(user);
    }

    @Override
    public Integer updatePassword(UserPasswordDTO userPasswordDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userPasswordDTO.getUsername());
        queryWrapper.eq("password", userPasswordDTO.getPassword());

        List<User> users = userMapper.selectList(queryWrapper);
        int update = users.size();
        if (update < 1) {
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }
        User user = userMapper.selectOne(queryWrapper);
        user.setPassword(userPasswordDTO.getNewPassword());
        return userMapper.updateById(user);
    }

    @Override
    public Result getHomeInfo() {
        Date date = new Date();
        DateTime tomorrow = DateUtil.tomorrow();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String str = simpleDateFormat.format(date);
        String str1 = simpleDateFormat.format(tomorrow);
        Long userCount = userMapper.selectCount(null);
        Double sale = ordersMapper.selectSale();
        if (sale == null) {
            sale = 0.0;
        }
        Integer integer = ordersMapper.selectSaleToday(str, str1);
        if (integer == null) {
            integer = 0;
        }
        HomeVO homeVO = new HomeVO(sale, userCount, integer);
        return Result.success(homeVO);
    }


    private List<Menu> getRoleMenus(String flag) {
        //查询角色id
        Integer roleId = roleMapper.selectByFlag(flag);
        //查询角色拥有的菜单id
        List<Integer> menuIds = roleMenuMapper.getRoleMenu(roleId);
        //查询所有菜单id
        List<Menu> menus = menuService.findMenues("");
        //角色菜单
        List<Menu> roleMenus = new ArrayList();
        //筛选当前用户的菜单
        for (Menu menu : menus) {
            //contains包含
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            //removeIf 移除 children里面不在menuIds集合中的元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}
