package com.cow.horse.service;


import com.cow.horse.commcn.Result;
import com.cow.horse.entity.Examine;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IExamineService extends IService<Examine> {

    Result findPage(Integer pageNum, Integer pageSize);

    Result inspect(Examine examine);
}
