package com.shihao.controller;


import com.shihao.domain.MiaoshaOrder;
import com.shihao.domain.MiaoshaUser;
import com.shihao.domain.OrderInfo;
import com.shihao.result.CodeMsg;
import com.shihao.service.GoodsService;
import com.shihao.service.MiaoshaService;
import com.shihao.service.OrderService;
import com.shihao.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @RequestMapping("/do_miaosha")
    public String miaosha(Model model, MiaoshaUser user,
                          @RequestParam("goodsId")Long goodsId){
        if(user == null){
            return "login";
        }
        //判断库存是否充足
        GoodsVo goods =  goodsService.getGoodsVoById(goodsId);
        if(goods.getStockCount()<0){
            model.addAttribute("errmsg", CodeMsg.MIAOSHA_ORVER.getMsg());
            return "miaosha_fail";
        }
        //判断是否已经秒杀到商品
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goods.getId());
        if(order!=null){
            model.addAttribute("errmsg", CodeMsg.MIAOSHA_REPEAT.getMsg());
            return "miaosha_fail";
        }
        //减库存、下订单、写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user,goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }
}
