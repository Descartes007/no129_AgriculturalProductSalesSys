package com.cow.horse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cow.horse.commcn.Result;
import com.cow.horse.entity.Cart;
import com.cow.horse.entity.User;
import com.cow.horse.utils.TokenUtils;
import com.cow.horse.mapper.CartMapper;
import com.cow.horse.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

    @Autowired
    CartMapper cartMapper;

    @Override
    public Page<Cart> findPage(String name, Integer pageNum, Integer pageSize) {

        User currentUser = TokenUtils.getCurrentUser();
        Integer userId = currentUser.getId();
        String role = currentUser.getRole();

        return cartMapper.page(new Page<>(pageNum, pageSize), userId, role, name);
    }

    @Override
    public void updateNum(Integer id, Integer num) {
        cartMapper.updateNum(id, num);
    }

    @Override
    public Result saveOrUpdateCart(Cart cart) {

        Integer productId = cart.getProductId();
        Integer userId = TokenUtils.getCurrentUser().getId();
        //更新购物车里相同的数量
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        queryWrapper.eq("user_id", userId);
        Cart cart1 = cartMapper.selectOne(queryWrapper);
        if (cart1 != null) {
            cart1.setNum(cart1.getNum() + cart.getNum());
            cartMapper.updateById(cart1);
            return Result.success();
        }
        //新增
        if (cart.getId() == null) {
            cart.setUserId(userId);
        }
        cartMapper.insert(cart);
        return Result.success();
    }

    @Override
    public List<Cart> findAll() {
        LambdaQueryWrapper<Cart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        Integer uid = TokenUtils.getCurrentUser().getId();
        lambdaQueryWrapper.eq(Cart::getUserId, uid);
        List<Cart> carts = cartMapper.selectList(lambdaQueryWrapper);
        return carts;
    }
}
