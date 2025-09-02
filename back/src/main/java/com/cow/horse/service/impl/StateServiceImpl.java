package com.cow.horse.service.impl;

import com.cow.horse.entity.State;
import com.cow.horse.mapper.StateMapper;
import com.cow.horse.service.IStateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class StateServiceImpl extends ServiceImpl<StateMapper, State> implements IStateService {

}
