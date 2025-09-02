package com.cow.horse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.config.AuthAccess;
import com.cow.horse.entity.Classification;
import com.cow.horse.entity.Product;
import com.cow.horse.utils.TokenUtils;
import com.cow.horse.mapper.ClassificationMapper;
import com.cow.horse.mapper.ProductMapper;
import com.cow.horse.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ClassificationMapper classificationMapper;

    @AuthAccess
    @Override
    public Page<Product> findPage(Integer pageNum, Integer pageSize, String name) {
        String role = TokenUtils.getCurrentUser().getRole();
        return productMapper.findPage(new Page<>(pageNum, pageSize), name, role);
    }

    @AuthAccess
    @Override
    public Integer seleclassId(String classificationName) {
        QueryWrapper<Classification> classificationQueryWrapper = new QueryWrapper<>();
        classificationQueryWrapper.eq("name", classificationName);
        Classification classification = classificationMapper.selectOne(classificationQueryWrapper);
        return classification.getId();
    }

    @AuthAccess
    @Override
    public Result findLovely(String city) {

        Integer uid = TokenUtils.getCurrentUser().getId();
         /*
        基于用户id查询用户近7天访问最多的商品种类id
        */
        Integer cid = productMapper.findLovely(uid);

        List<Product> list = productMapper.listByCid(cid, city);

        return Result.success(list);
    }

    @AuthAccess
    @Override
    public Result deleteProduct(Integer id) {
        Product product = findProductById(id);
        product.setIsDelete(true);
        return Result.success(productMapper.updateById(product));
    }

    @AuthAccess
    @Override
    public Boolean deleteProducts(List<Integer> ids) {
        for (Integer id : ids) {
            Product product = findProductById(id);
            product.setIsDelete(true);
            productMapper.updateById(product);
        }
        return true;
    }

    @AuthAccess
    @Override
    public Result findRecommend(String city) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_daily", true);
        queryWrapper.eq("city", city);
        return Result.success(productMapper.selectList(queryWrapper));
    }

    @Override
    public Page<Product> findPages(Integer pageNum, Integer pageSize, String name, String city) {
        return productMapper.findPages(new Page<>(pageNum, pageSize), name, city);
    }

    @AuthAccess
    public Product findProductById(Integer productId) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", productId);
        Product product = productMapper.selectOne(queryWrapper);
        return product;
    }
}
