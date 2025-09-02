package com.cow.horse.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cow.horse.commcn.Result;
import com.cow.horse.controller.dto.AliPay;
import com.cow.horse.entity.Orders;
import com.cow.horse.entity.State;
import com.cow.horse.entity.User;
import com.cow.horse.utils.TokenUtils;
import com.cow.horse.entity.Examine;
import com.cow.horse.mapper.ExamineMapper;
import com.cow.horse.mapper.OrdersMapper;
import com.cow.horse.mapper.StateMapper;
import com.cow.horse.service.IExamineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cow.horse.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class ExamineServiceImpl extends ServiceImpl<ExamineMapper, Examine> implements IExamineService {

    @Autowired
    ExamineMapper examineMapper;

    @Autowired
    OrdersMapper ordersMapper;

    @Autowired
    StateMapper stateMapper;

    @Resource
    private IOrdersService ordersService;

    @Override
    public Result findPage(Integer pageNum, Integer pageSize) {
        User currentUser = TokenUtils.getCurrentUser();
        return Result.success(examineMapper.findPage(new Page<>(pageNum, pageSize), currentUser.getRole(), currentUser.getId()));
    }

    //审核
    @Override
    public Result inspect(Examine examine)  {

        Integer orderId = examine.getOrderId();
        String operation = examine.getOperation();
        Orders orders = ordersMapper.selectById(orderId);
        String passno = examine.getPassno();
        if (operation.equals("取消订单")) {
            //审核通过
            if (passno.equals("审核通过")) {
                orders.setState(8);
            } else {
                //审核不通过
                QueryWrapper<State> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("name", examine.getItemStatus());
                State state = stateMapper.selectOne(queryWrapper);
                orders.setState(state.getId());
            }
            ordersMapper.updateById(orders);
            examineMapper.updateById(examine);
        }
        if (operation.equals("申请退款")) {

            String now = DateUtil.now();
            if (orders != null) {
                // hutool工具类，判断时间间隔
                long between = DateUtil.between(DateUtil.parseDateTime(orders.getDeliveryTime()), DateUtil.parseDateTime(now), DateUnit.DAY);
                if (between > 3) {
                    return Result.error("-1", "该订单已超过3天，不支持退款");
                }
            }

            //审核通过
            if (passno.equals("审核通过")) {

                AliPay aliPay = new AliPay();
                String alipayNo = orders.getAlipayNo();
                aliPay.setAlipayTraceNo(alipayNo);
                String allPrice = String.valueOf(orders.getAllPrice());
                aliPay.setTotalAmount(allPrice);
                aliPay.setTraceNo(orders.getNo());
//                ordersService.returnPay(aliPay);
                orders.setState(7);
            } else {
                //审核不通过
                QueryWrapper<State> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("name", examine.getItemStatus());
                State state = stateMapper.selectOne(queryWrapper);
                orders.setState(state.getId());
            }
            ordersMapper.updateById(orders);
            examineMapper.updateById(examine);
        }
        return Result.success();
    }
}
