package com.cow.horse.controller.dto;

import cn.hutool.core.annotation.Alias;
import com.cow.horse.entity.Menu;
import lombok.Data;

import java.util.List;

//DTO用来接收前端返回的数据

/**
 * //        User one = TokenUtils.getCurrentUser();
 * //        System.out.println("==========>" + one.getUsername());  获取用户方法
 * 接收前端登录请求的参数
 */
@Data
public class UserDTO {
    @Alias("id")
    private Integer id;
    @Alias("用户名")
    private String username;
    @Alias("密码")
    private String password;
    @Alias("昵称")
    private String nickname;
    @Alias("头像")
    private String avatarUrl;

    private String token;
    @Alias("角色")
    private String role;

    private List<Menu> menus;
}
