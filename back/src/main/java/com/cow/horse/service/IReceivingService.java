package com.cow.horse.service;

import com.cow.horse.entity.Receiving;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface IReceivingService extends IService<Receiving> {

    Boolean saveOrUpdateReceiving(Receiving receiving);

    List<Receiving> findAll();
}
