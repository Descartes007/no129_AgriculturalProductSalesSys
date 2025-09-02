package com.cow.horse.controller.dto;

import lombok.Data;

@Data
public class UserPasswordDTO {
    private String username;
    private String password;
    private String newPassword;
}
