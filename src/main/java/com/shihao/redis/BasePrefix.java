package com.shihao.redis;

public abstract class BasePrefix implements KeyPrefix {

    private int expireSecodes;
    private String prefix;

    //永不过期
    public BasePrefix(String prefix) {
        this(0,prefix);
    }

    public BasePrefix(int expireSecodes, String prefix) {
        this.expireSecodes = expireSecodes;
        this.prefix = prefix;
    }

    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+":" + prefix;
    }

    public int expireSeconds() {
        return expireSecodes;
    }
}
