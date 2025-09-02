package com.cow.horse.service.impl;

import cn.hutool.core.date.DateUtil;
import com.cow.horse.commcn.Result;
import com.cow.horse.entity.Orders;
import com.cow.horse.entity.User;
import com.cow.horse.service.ICommentService;
import com.cow.horse.utils.TokenUtils;
import com.cow.horse.entity.Comment;
import com.cow.horse.mapper.CommentMapper;
import com.cow.horse.mapper.OrdersMapper;
import com.cow.horse.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    OrdersMapper ordersMapper;

    @Override
    public Result findTree(Integer productId) {
        //商品id查评论 所有评论 和回复数据
        List<Comment> commentDetail = commentMapper.findCommentDetail(productId);

        //查询评论数据 不包括回复
        List<Comment> ancestorsList = commentDetail.stream().filter(comment -> comment.getAncestorsId() == null).collect(Collectors.toList());

        //查询回复内容
        for (Comment origin : ancestorsList) {
            List<Comment> comments = commentDetail.stream().filter(comment -> origin.getId().equals(comment.getAncestorsId())).collect(Collectors.toList());// 表示回复对象集合
            //回复对象的集合
            comments.forEach(comment -> {
                commentDetail.stream().filter(c1 -> c1.getId().equals(comment.getPid())).findFirst().ifPresent((v ->                // 找到当前评论的父级
                {
                    comment.setPUserId(v.getPUserId());
                }));
            });
            origin.setChildren(comments);
        }
        return Result.success(ancestorsList);
    }

    @Override
    public int saveOrUpdateByComment(Comment comment) {
        //新增
        User currentUser = TokenUtils.getCurrentUser();
        if (comment.getId() == null) {
            Integer uid = currentUser.getId();
            comment.setUserId(uid);
            comment.setTime(DateUtil.now());
            //只是评论 改变状态
            if (comment.getOrderId() != null) {
                Orders orders = ordersMapper.selectById(comment.getOrderId());
                orders.setState(6);
                ordersMapper.updateById(orders);
            }

            if (comment.getPid() != null) { // 判断如果是回复，进行处理
                Integer pid = comment.getPid();
                Comment pComment = commentMapper.selectById(pid);
                Integer pUserId = pComment.getUserId();
                User pUser = userMapper.selectById(pUserId);
                comment.setPUserName(pUser.getNickname());
                if (pComment.getAncestorsId() != null) {// 如果当前回复的父级有祖宗，那么就设置相同的祖宗
                    comment.setAncestorsId(pComment.getAncestorsId());
                } else {// 否则就设置父级为当前回复的祖宗
                    comment.setAncestorsId(comment.getPid());
                }
            }

            return commentMapper.insert(comment);
        }
        return commentMapper.updateById(comment);
    }
}
