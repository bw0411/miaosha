package com.shihao.service;


import com.shihao.domain.MiaoshaUser;
import com.shihao.domain.OrderInfo;
import com.shihao.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减少库存
        goodsService.reduceStock(goods);
        //生成订单和秒杀订单
        OrderInfo orderInfo = orderService.createOrderInfo(user,goods);
        return orderInfo;
    }
}
