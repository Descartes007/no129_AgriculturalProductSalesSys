package com.cow.horse.service;

import com.cow.horse.entity.LiveProduct;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface ILiveProductService extends IService<LiveProduct> {

    List<LiveProduct> findAll(Integer liveId);
}
