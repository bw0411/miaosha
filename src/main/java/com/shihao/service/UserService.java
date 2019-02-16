package com.shihao.service;

import com.shihao.dao.UserDao;
import com.shihao.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User findById(Integer id){
       return userDao.findById(id);
    }
}
