package com.cow.horse.service.impl;

import com.cow.horse.entity.LiveProduct;
import com.cow.horse.mapper.LiveProductMapper;
import com.cow.horse.service.ILiveProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LiveProductServiceImpl extends ServiceImpl<LiveProductMapper, LiveProduct> implements ILiveProductService {

    @Autowired
    LiveProductMapper liveProductMapper;

    @Override
    public List<LiveProduct> findAll(Integer liveId) {
        return liveProductMapper.findAll(liveId);
    }
}
