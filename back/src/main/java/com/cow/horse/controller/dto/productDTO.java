package com.cow.horse.controller.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class productDTO {
    private Integer id;
    private String name;
    private String url;
    private String description;
    private BigDecimal sellPrice;
}
