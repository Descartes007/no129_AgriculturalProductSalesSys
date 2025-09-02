package com.cow.horse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cow.horse.commcn.Result;
import com.cow.horse.config.AuthAccess;
import com.cow.horse.entity.Article;
import com.cow.horse.mapper.ArticleMapper;
import com.cow.horse.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Autowired
    ArticleMapper articleMapper;

    @AuthAccess
    @Override
    public Result findAll() {

        QueryWrapper<Article> queryWrapper = new QueryWrapper();
        queryWrapper.eq("enable", true);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(articles);
    }
}
