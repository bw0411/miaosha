package com.shihao.config;

import com.shihao.domain.MiaoshaUser;
import com.shihao.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    MiaoshaUserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return MiaoshaUser.class == methodParameter.getParameterType();
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        //从参数中获取token(有些移动客户端将token放到参数中带到服务器)
        String paramToken = request.getParameter(MiaoshaUserService.TOKEN_NAME);
        //从cookie中获取token
        String cookieToken = getCookieValue(request,MiaoshaUserService.TOKEN_NAME);
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return null;
        }
        //设置token优先级
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        //从redis中获取user对象
        MiaoshaUser user = userService.getByToken(token,response);
        return user;
    }

    private String getCookieValue(HttpServletRequest request, String tokeName) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie:cookies){
            if(cookie.getName().equals(tokeName)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
