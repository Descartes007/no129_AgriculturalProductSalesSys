package com.cow.horse.controller.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class orderDTO {
    private Integer productId;
    private String productName;
    private BigDecimal allPrice;
    private Integer num;
}
