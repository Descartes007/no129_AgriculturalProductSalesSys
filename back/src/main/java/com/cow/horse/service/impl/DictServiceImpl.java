package com.cow.horse.service.impl;

import com.cow.horse.entity.Dict;
import com.cow.horse.service.IDictService;
import com.cow.horse.mapper.DictMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

}
