package com.cow.horse.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class HomeMenuResultDTO {
    private Integer id;
    private String name;

    //商品
    private List<productDTO> productDTOList;
}
