package com.cow.horse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cow.horse.commcn.Result;
import com.cow.horse.config.AuthAccess;
import com.cow.horse.entity.ProductImg;
import com.cow.horse.mapper.ProductImgMapper;
import com.cow.horse.service.IProductImgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductImgServiceImpl extends ServiceImpl<ProductImgMapper, ProductImg> implements IProductImgService {

    @Autowired
    ProductImgMapper productImgMapper;

    @AuthAccess
    @Override
    public List<ProductImg> findImgList(Integer id) {
        QueryWrapper<ProductImg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", id);
        List<ProductImg> productImgs = productImgMapper.selectList(queryWrapper);
        return productImgs;
    }

    @Override
    public Result saveImg(Integer productId, List<String> urlList) {
        for (String s : urlList) {
            ProductImg productImg = new ProductImg();
            productImg.setUrl(s);
            productImg.setProductId(productId);
            productImgMapper.insert(productImg);
        }
        return Result.success();
    }
}
