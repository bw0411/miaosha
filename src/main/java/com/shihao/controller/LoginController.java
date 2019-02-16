package com.shihao.controller;


import com.shihao.rabbitmq.MQReceiver;
import com.shihao.rabbitmq.MQSender;
import com.shihao.result.Result;
import com.shihao.service.MiaoshaUserService;
import com.shihao.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {


    @Autowired
    MQSender sender;

    @Autowired
    MiaoshaUserService userService;

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }


    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
        //登录
        sender.sender("1111111111111");

        sender.sendTopic("topicMessage");
        boolean result = userService.login(response,loginVo);
        return Result.success(true);
    }
}
