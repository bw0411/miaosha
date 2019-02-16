package com.shihao.redis;

public interface KeyPrefix {

    public String getPrefix();

    public int expireSeconds();
}
