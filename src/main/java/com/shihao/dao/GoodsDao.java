package com.shihao.dao;

import com.shihao.domain.MiaoshaGoods;
import com.shihao.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsDao {

    @Select("select mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price,g.* from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    public List<GoodsVo> listGoodsVo();

    @Select("select mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price,g.* " +
            " from miaosha_goods mg left join goods g on mg.goods_id = g.id" +
            " where g.id = #{goodsId}")
    public GoodsVo getGoodsVoById(@Param("goodsId") Long goodsId);

    @Update("update miaosha_goods set stock_count = stock_count-1 where goods_id = #{goodsId} and stcok_count > 0")
    public void reduceStock(MiaoshaGoods miaoshaGoods);
}
