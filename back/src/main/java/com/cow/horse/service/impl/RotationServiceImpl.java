package com.cow.horse.service.impl;

import com.cow.horse.entity.Rotation;
import com.cow.horse.mapper.RotationMapper;
import com.cow.horse.service.IRotationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class RotationServiceImpl extends ServiceImpl<RotationMapper, Rotation> implements IRotationService {

}
