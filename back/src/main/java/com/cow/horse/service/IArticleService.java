package com.cow.horse.service;

import com.cow.horse.commcn.Result;
import com.cow.horse.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IArticleService extends IService<Article> {

    Result findAll();
}
