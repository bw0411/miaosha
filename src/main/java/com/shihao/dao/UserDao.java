package com.shihao.dao;

import com.shihao.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao{

    @Select("select * from user where id = #{id}")
    public User findById(@Param("id")Integer id);
}
