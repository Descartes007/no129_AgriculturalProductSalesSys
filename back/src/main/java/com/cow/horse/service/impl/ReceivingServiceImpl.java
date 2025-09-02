package com.cow.horse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cow.horse.entity.User;
import com.cow.horse.utils.TokenUtils;
import com.cow.horse.entity.Receiving;
import com.cow.horse.mapper.ReceivingMapper;
import com.cow.horse.service.IReceivingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class ReceivingServiceImpl extends ServiceImpl<ReceivingMapper, Receiving> implements IReceivingService {

    @Resource
    ReceivingMapper receivingMapper;

    @Override
    public Boolean saveOrUpdateReceiving(Receiving receiving) {
        //新增
        if (receiving.getId() == null) {
            User currentUser = TokenUtils.getCurrentUser();
            receiving.setUserId(currentUser.getId());
            int insert = receivingMapper.insert(receiving);
            if (insert >= 1) {
                return true;
            }
        } else {
            int i = receivingMapper.updateById(receiving);
            if (i >= 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Receiving> findAll() {
        User currentUser = TokenUtils.getCurrentUser();
        QueryWrapper<Receiving> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", currentUser.getId());
        return receivingMapper.selectList(queryWrapper);
    }
}
