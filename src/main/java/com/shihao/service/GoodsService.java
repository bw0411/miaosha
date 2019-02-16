package com.shihao.service;


import com.shihao.dao.GoodsDao;
import com.shihao.domain.MiaoshaGoods;
import com.shihao.redis.GoodsKey;
import com.shihao.redis.RedisService;
import com.shihao.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    @Autowired
    RedisService redisService;

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoById(Long goodsId) {
        GoodsVo goodsVo = redisService.get(GoodsKey.getGoodsVoById,""+goodsId,GoodsVo.class);
        if(goodsVo!=null){
            return goodsVo;
        }
        goodsVo = goodsDao.getGoodsVoById(goodsId);
        if(goodsVo!=null){
            redisService.set(GoodsKey.getGoodsVoById,""+goodsId,GoodsVo.class);
        }
        return goodsVo;
    }

    /**
     * 解决一个人秒杀多个商品（在秒杀商品表中添加唯一策略）
     * @param goods
     */
    public void reduceStock(GoodsVo goods) {
        MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
        miaoshaGoods.setGoodsId(goods.getId());
        miaoshaGoods.setStockCount(goods.getStockCount()-1);
        goodsDao.reduceStock(miaoshaGoods);
    }
}
