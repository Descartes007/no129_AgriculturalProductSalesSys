package com.cow.horse.service;

import com.cow.horse.commcn.Result;
import com.cow.horse.controller.dto.UserDTO;
import com.cow.horse.controller.dto.UserPasswordDTO;
import com.cow.horse.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    Result register(UserDTO userDTO);

    Result getUserByName(String username);

    Integer updatePassword(UserPasswordDTO userPasswordDTO);

    Result getHomeInfo();

}
