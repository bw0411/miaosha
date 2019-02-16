package com.shihao.controller;

import com.shihao.domain.User;
import com.shihao.redis.RedisService;
import com.shihao.redis.UserKey;
import com.shihao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @GetMapping("/selectById")
    public String findById(){
        User user = userService.findById(1);
        System.out.println(user.getId()+"========"+user.getName());
        boolean result = redisService.set(UserKey.getById,user.getId()+"",user.getName());
        if (result){
            User user1 = (User)redisService.get(UserKey.getById,user.getId()+"",User.class);
            return user1.getName();
        }
        return "11";
    }
}
