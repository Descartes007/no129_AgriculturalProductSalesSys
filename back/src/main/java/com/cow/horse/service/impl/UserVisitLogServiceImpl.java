package com.cow.horse.service.impl;

import com.cow.horse.commcn.Constants;
import com.cow.horse.commcn.Result;
import com.cow.horse.entity.Product;
import com.cow.horse.entity.UserVisitLog;
import com.cow.horse.utils.TokenUtils;
import com.cow.horse.mapper.ProductMapper;
import com.cow.horse.mapper.UserVisitLogMapper;
import com.cow.horse.service.IUserVisitLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserVisitLogServiceImpl extends ServiceImpl<UserVisitLogMapper, UserVisitLog> implements IUserVisitLogService {

    @Autowired
    UserVisitLogMapper userVisitLogMapper;

    @Autowired
    ProductMapper productMapper;

    //保存用户浏览记录
    @Override
    public Result saveUserVisitLog(UserVisitLog userVisitLog) {

        Integer userid = TokenUtils.getCurrentUser().getId();
        Integer pid = userVisitLog.getPid();
        Product product = productMapper.selectById(pid);
        Integer classificationId = product.getClassificationId();
        userVisitLog.setUid(userid);
        userVisitLog.setCid(classificationId);
        if (userVisitLogMapper.insert(userVisitLog) >= 1) {
            return Result.success();
        } else {
            return Result.error(Constants.CODE_600, "其他业务异常");
        }

    }
}
