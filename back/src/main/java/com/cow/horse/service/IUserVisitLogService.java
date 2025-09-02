package com.cow.horse.service;

import com.cow.horse.commcn.Result;
import com.cow.horse.entity.UserVisitLog;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IUserVisitLogService extends IService<UserVisitLog> {

    Result saveUserVisitLog(UserVisitLog userVisitLog);
}
