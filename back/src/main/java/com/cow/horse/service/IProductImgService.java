package com.cow.horse.service;

import com.cow.horse.commcn.Result;
import com.cow.horse.entity.ProductImg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface IProductImgService extends IService<ProductImg> {

    List<ProductImg> findImgList(Integer id);

    Result saveImg(Integer productId, List<String> urlList);
}
