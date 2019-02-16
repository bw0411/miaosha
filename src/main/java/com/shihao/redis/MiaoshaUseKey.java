package com.shihao.redis;

public class MiaoshaUseKey extends BasePrefix {

    public final static int TOKEN_EXPIRE = 3600 * 24 * 2;

    public MiaoshaUseKey(int expireSecodes, String prefix) {
        super(expireSecodes, prefix);
    }

    public static MiaoshaUseKey token  = new MiaoshaUseKey(TOKEN_EXPIRE,"tk");
    public static MiaoshaUseKey getById  = new MiaoshaUseKey(TOKEN_EXPIRE,"getById");
}
