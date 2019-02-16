package com.shihao.redis;

public class GoodsKey extends BasePrefix {

    public GoodsKey(String prefix) {
        super(prefix);
    }

    public GoodsKey(int expireSecodes, String prefix) {
        super(expireSecodes, prefix);
    }

    public static GoodsKey goodsList = new GoodsKey(30,"goodsList");
    public static GoodsKey getById = new GoodsKey(30,"getById");
    public static GoodsKey getGoodsVoById = new GoodsKey(30,"getGoodsVoById");
}
