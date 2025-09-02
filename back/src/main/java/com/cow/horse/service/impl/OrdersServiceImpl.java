package com.cow.horse.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.controller.dto.AliPay;
import com.cow.horse.entity.*;
import com.cow.horse.mapper.*;
import com.cow.horse.utils.TokenUtils;

import com.cow.horse.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrdersProductMapper ordersProductMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StateMapper stateMapper;

    @Autowired
    private ExamineMapper examineMapper;
    @Autowired
    private ReceivingMapper receivingMapper;

    @Autowired
    private ClassificationMapper classificationMapper;

    private static final String GATEWAY_URL = "https://openapi.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    //签名方式
    private static final String SIGN_TYPE = "RSA2";




    @Override
    public Page<Orders> finPage(Integer pageNum, Integer pageSize, String name, String stateName) {
        Integer stateId = selectStateById(stateName);
        User currentUser = TokenUtils.getCurrentUser();
        return ordersMapper.finPage(new Page<>(pageNum, pageSize), name, currentUser.getRole(), currentUser.getId(), stateId);
    }

    @Override
    public Result saveOrUpdateOrders(Orders orders) {
        //新增
        Integer receivingId = orders.getReceivingId();
        Receiving receiving = receivingMapper.selectById(receivingId);

        if (orders.getId() == null) {
            List<Cart> carts = orders.getCarts();
            for (Cart cart : carts) {
                List<Cart> cartArrayList = new ArrayList<>();
                Orders orders1 = new Orders();

                Integer userId = TokenUtils.getCurrentUser().getId();
                Integer num = cart.getNum();
                BigDecimal num1 = new BigDecimal(num.toString());
                BigDecimal productPrice = cart.getProductPrice();
                BigDecimal i = num1.multiply(productPrice);
                orders1.setAllPrice(i);
                orders1.setName(cart.getProductName());
                orders1.setUserId(userId);
                orders1.setPhone(receiving.getPhone());
                orders1.setAddress(receiving.getAddress());
                orders1.setFullName(receiving.getUsername());
                Date data = new Date();
                orders1.setNo(DateUtil.format(data, "yyyyMMdd") + System.currentTimeMillis());
                orders1.setState(2);
                //创建订单
                cartArrayList.add(cart);
                orders1.setCarts(cartArrayList);
                ordersMapper.insert(orders1);


                OrdersProduct ordersProduct = new OrdersProduct();
                //修改商品库存数量销量
                Product product = productMapper.selectById(cart.getProductId());
                product.setStore(product.getStore() - cart.getNum());
                product.setSoldNum(product.getSoldNum() + cart.getNum());
                productMapper.updateById(product);

                ordersProduct.setProductId(cart.getProductId());
                ordersProduct.setNum(cart.getNum());
                ordersProduct.setOrderId(orders1.getId());
                ordersProductMapper.insert(ordersProduct);
                //删除购物车数据
                cartMapper.deleteById(cart.getId());
            }
            //更新
        } else {
            Integer stateId = selectStateById(orders.getStateName());
            orders.setState(stateId);
            ordersMapper.updateById(orders);
        }
        return Result.success();
    }

    @Override
    public Result Examine(Examine examine) {
        Integer orderId = examine.getOrderId();
        Orders orders = ordersMapper.selectById(orderId);
        Integer userId = orders.getUserId();
        orders.setState(9);
        ordersMapper.updateById(orders);
        examine.setUserId(userId);
        int i = examineMapper.insert(examine);
        if (i >= 1) {
            return Result.success();
        } else {
            return Result.error("CODE_600", "操作失败");
        }
    }

    //逻辑删
    @Override
    public Result logicalDeletion(Integer id) {
        Orders orders = ordersMapper.selectById(id);
        orders.setIsDelete(true);
        ordersMapper.updateById(orders);
        return Result.success();
    }

    @Override
    public Result findStateName(String stateName) {
        QueryWrapper<State> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", stateName);
        State state = stateMapper.selectOne(queryWrapper);
        Integer stateId = state.getId();
        Integer uid = TokenUtils.getCurrentUser().getId();
        QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
        ordersQueryWrapper.eq("state", stateId);
        ordersQueryWrapper.eq("user_id", uid);
        return Result.success(ordersMapper.selectCount(ordersQueryWrapper));
    }

    @Override
    public Integer payment(String tradeNo, String gmtPayment, String alipayTradeNo) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("no", tradeNo);
        Orders orders = ordersMapper.selectOne(queryWrapper);
        orders.setPayTime(gmtPayment);
        orders.setAlipayNo(alipayTradeNo);
        orders.setState(orders.getState() + 1);
        int i = ordersMapper.updateById(orders);
        return i;
    }

    @Override
    public Integer updatePayState(String traceNo) {
        return null;
    }


    private Integer selectStateById(String stateName) {
        QueryWrapper<State> queryWrapper = new QueryWrapper<>();
        Integer stateId;
        if (!stateName.equals("全部")) {
            queryWrapper.eq("name", stateName);
            State selectOne = stateMapper.selectOne(queryWrapper);
            stateId = selectOne.getId();
        } else {
            stateId = 0;
        }
        return stateId;
    }




    @Override
    public Boolean cancelOrder(Integer id) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Orders orders = ordersMapper.selectOne(queryWrapper);
        orders.setState(8);
        ordersMapper.updateById(orders);
        return true;
    }

    @Override
    public Result buy(Orders orders) {
        Integer receivingId = orders.getReceivingId();
        Receiving receiving = receivingMapper.selectById(receivingId);

        Integer userId = TokenUtils.getCurrentUser().getId();
        orders.setUserId(userId);
        Date data = new Date();
        orders.setPhone(receiving.getPhone());
        orders.setAddress(receiving.getAddress());
        orders.setFullName(receiving.getUsername());
        orders.setNo(DateUtil.format(data, "yyyyMMdd") + System.currentTimeMillis());
        orders.setState(2);
        ordersMapper.insert(orders);
        String no = orders.getNo();


        OrdersProduct ordersProduct = new OrdersProduct();
        //修改商品库存数量销量
        Product product = productMapper.selectById(orders.getProductId());
        product.setStore(product.getStore() - orders.getNum());
        product.setSoldNum(product.getSoldNum() + orders.getNum());
        productMapper.updateById(product);

        ordersProduct.setProductId(orders.getProductId());
        ordersProduct.setNum(orders.getNum());
        ordersProduct.setOrderId(orders.getId());
        ordersProductMapper.insert(ordersProduct);

        return Result.success(no);
    }

    @Override
    public Result members() {
        List<Series> seriesList = new ArrayList<>();
        String beginOfMonth = DateUtil.format(DateUtil.beginOfMonth(DateUtil.date()), "yyyy-MM-dd");
        String endOfMonth = DateUtil.format(DateUtil.endOfMonth(DateUtil.date()), "yyyy-MM-dd");
        List<Classification> classifications = classificationMapper.selectList(null);
        for (Classification classification : classifications) {
            List<String> members = ordersMapper.members(beginOfMonth, endOfMonth, classification.getName());
            seriesList.add(new Series(classification.getName(), members, "line"));
//            seriesList.add(new Series(classification.getName(), members, "bar"));
        }
        List<String> dateTime = ordersMapper.dateTime(beginOfMonth, endOfMonth);
        AnalysisVO analysisVO = new AnalysisVO();
        analysisVO.setXAxis(dateTime);
        analysisVO.setSeries(seriesList);
        return Result.success(analysisVO);
    }

}
