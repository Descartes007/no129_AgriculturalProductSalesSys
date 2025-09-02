package com.cow.horse.service;

import com.cow.horse.commcn.Result;
import com.cow.horse.controller.dto.HomeMenuResultDTO;
import com.cow.horse.entity.Classification;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface IClassificationService extends IService<Classification> {

    List<HomeMenuResultDTO> getMenus();

    Result classification(Integer id);
}
