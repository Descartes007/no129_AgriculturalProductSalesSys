package com.cow.horse.service.impl;

import com.cow.horse.entity.Live;
import com.cow.horse.mapper.LiveMapper;
import com.cow.horse.service.ILiveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class LiveServiceImpl extends ServiceImpl<LiveMapper, Live> implements ILiveService {

}
