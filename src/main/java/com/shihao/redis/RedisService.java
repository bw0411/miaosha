package com.shihao.redis;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import sun.security.util.Length;

@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    public <T> T get(KeyPrefix keyPrefix,String key,Class<T> clazz){
        Jedis jedis = jedisPool.getResource();
        try {
            String realKey = keyPrefix.getPrefix()+key;
            if(StringUtils.isEmpty(realKey)){
                return null;
            }
            String result = jedis.get(realKey);
            return stringToBean(result,clazz);
        }finally {
            returnToPool(jedis);
        }
    }


    public <T> boolean set(KeyPrefix keyPrefix,String key,T value){
        Jedis jedis = jedisPool.getResource();
        try {
            int expireSecodes = keyPrefix.expireSeconds();
            String realKey = keyPrefix.getPrefix()+key;
            if(StringUtils.isEmpty(realKey)){
                return false;
            }
            String str = beanToString(value);
            if(expireSecodes <= 0){
                jedis.set(realKey,str);
            }else{
                jedis.setex(realKey,expireSecodes,str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }


    /**
     * 判断key是否存在
     * */
    public <T> boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    private void returnToPool(Jedis jedis) {
        if (jedis != null){
            jedis.close();
        }
    }

    /**
     * 增加值
     * */
    public <T> Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少值
     * */
    public <T> Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    public static <T> String beanToString(T value){
        if(value == null){
            return null;
        }
        Class clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class){
            return value+"";
        }else if(clazz == long.class || clazz == Long.class){
            return value+"";
        }else{
            return JSON.toJSONString(value);
        }
    }

    public static <T> T stringToBean(String value,Class<T> clazz){
        if(value == null){
            return null;
        }
        if (clazz == int.class || clazz == Integer.class){
            return (T) Integer.valueOf(value);
        }else if(clazz == long.class || clazz == Long.class){
            return (T) Long.valueOf(value);
        }else{
            return JSON.parseObject(value,clazz);
        }
    }

    public boolean delete(KeyPrefix keyPrefix, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            String realKey = keyPrefix.getPrefix()+value;
            if(StringUtils.isEmpty(realKey)){
                return false;
            }
            jedis.del(realKey);
            return true;
        }finally {
            returnToPool(jedis);
        }
    }
}
