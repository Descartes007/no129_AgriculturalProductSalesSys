package com.cow.horse.service;

import com.cow.horse.commcn.Result;
import com.cow.horse.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;


public interface ICommentService extends IService<Comment> {

    Result findTree(Integer productId);

    int saveOrUpdateByComment(Comment comment);
}
