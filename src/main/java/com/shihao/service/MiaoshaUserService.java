package com.shihao.service;

import com.shihao.dao.MiaoshaUserDao;
import com.shihao.domain.MiaoshaUser;
import com.shihao.exception.GlobalException;
import com.shihao.redis.MiaoshaUseKey;
import com.shihao.redis.RedisService;
import com.shihao.result.CodeMsg;
import com.shihao.utils.MD5Util;
import com.shihao.utils.UUIDUtil;
import com.shihao.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {

    public static final String TOKEN_NAME = "token";

    @Autowired
    MiaoshaUserDao userDao;

    @Autowired
    RedisService redisService;

    public boolean login(HttpServletResponse response,LoginVo loginVo) {
        if(loginVo == null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPassword = loginVo.getPassword();
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if(user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPassword = MD5Util.formPassToDBPass(formPassword,user.getSalt());
        if(!dbPassword.equals(user.getPassword())){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //添加cookie
        String token = UUIDUtil.randomUUID();
        addCookie(response,token,user);
        return true;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        redisService.set(MiaoshaUseKey.token,token,user);
        Cookie cookie = new Cookie(TOKEN_NAME, token);
        //设置cookie有效期
        cookie.setMaxAge(MiaoshaUseKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public MiaoshaUser getByToken(String token, HttpServletResponse response) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUseKey.token,token,MiaoshaUser.class);
        //延长Cookie有效期
        if(user != null){
            addCookie(response,token,user);
        }
        return user;
    }

    public MiaoshaUser getById(Long id) {
        MiaoshaUser user = redisService.get(MiaoshaUseKey.getById,id+"",MiaoshaUser.class);
        //延长Cookie有效期
        if(user != null){
            return user;
        }
        user = userDao.getById(id);
        if(user!=null){
            redisService.set(MiaoshaUseKey.getById,id+"",user);
        }
        return user;
    }


    public MiaoshaUser updatePwd(String token,Long id,String formPass) {
       MiaoshaUser user = getById(id);
       if(user == null){
           throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
       }
       MiaoshaUser updateUser = new MiaoshaUser();
       updateUser.setId(id);
       String dbPass = MD5Util.formPassToDBPass(formPass,user.getSalt());
       updateUser.setPassword(dbPass);
       userDao.updatePwd(updateUser);

        //处理缓存
        redisService.delete(MiaoshaUseKey.getById,id+"");
        user.setPassword(dbPass);
        redisService.set(MiaoshaUseKey.token,token,user);
        return user;
    }
}
