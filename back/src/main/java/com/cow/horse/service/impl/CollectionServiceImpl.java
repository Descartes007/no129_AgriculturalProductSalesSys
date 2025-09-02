package com.cow.horse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cow.horse.commcn.Constants;
import com.cow.horse.commcn.Result;
import com.cow.horse.entity.Product;
import com.cow.horse.entity.User;
import com.cow.horse.utils.TokenUtils;
import com.cow.horse.entity.Collection;
import com.cow.horse.mapper.CollectionMapper;
import com.cow.horse.mapper.ProductMapper;
import com.cow.horse.mapper.UserMapper;
import com.cow.horse.service.ICollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements ICollectionService {

    @Autowired
    CollectionMapper collectionMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ProductMapper productMapper;

    @Override
    public Page<Collection> findPage(Integer pageNum, Integer pageSize, String name) {
        return collectionMapper.findPage(new Page<>(pageNum, pageSize), name);
    }

    @Override
    public List<Product> findAllByUserId(String username) {
        User user = getUser(username);
        Integer userid = user.getId();
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userid);
        List<Collection> collectionList = collectionMapper.selectList(queryWrapper);
        List<Product> productList = new ArrayList();
        for (Collection collection : collectionList) {
            Product product = productMapper.selectById(collection.getProductId());
            productList.add(product);
        }
        return productList;
    }

    @Override
    public Result saveCollection(Collection collection) {
        Integer userid = TokenUtils.getCurrentUser().getId();
        collection.setUserId(userid);

        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userid);
        queryWrapper.eq("product_id", collection.getProductId());
        Collection one = collectionMapper.selectOne(queryWrapper);
        if (one != null) {
            return Result.error(Constants.CODE_600, "已存收藏过了");
        }
        return Result.success(collectionMapper.insert(collection));
    }

    @Override
    public Result delete(Integer productId, String username) {
        User user = getUser(username);
        Integer userid = user.getId();
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userid);
        queryWrapper.eq("product_id", productId);
        Collection one = collectionMapper.selectOne(queryWrapper);
        return Result.success(collectionMapper.deleteById(one));
    }

    @Override
    public Result findPageReception(Integer pageNum, Integer pageSize, String productName, String username) {
        User user = getUser(username);
        Integer userid = user.getId();
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userid);
        List<Product> productList = new ArrayList();
        List<Collection> collectionList = collectionMapper.selectList(queryWrapper);
        for (Collection collection : collectionList) {
            QueryWrapper<Product> queryWrapper1 = new QueryWrapper<>();
            if (!"".equals(productName)) {
                queryWrapper1.like("name", productName);
            }
            queryWrapper1.eq("id", collection.getProductId());
            queryWrapper1.eq("is_delete", 0);
            List<Product> product = productMapper.selectList(queryWrapper1);
            for (Product product1 : product) {
                productList.add(product1);
            }
        }
        return Result.success(productList);
    }

    @Override
    public Result selectCollection(String productId, String username) {
        User user = getUser(username);
        Integer userid = user.getId();
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userid);
        queryWrapper.eq("product_id", productId);
        Long count = collectionMapper.selectCount(queryWrapper);
        if (count <= 0) {
            return Result.success(false);
        } else {
            return Result.success(true);
        }
    }

    private User getUser(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

}
