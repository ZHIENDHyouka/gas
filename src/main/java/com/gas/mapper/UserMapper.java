package com.gas.mapper;

import com.gas.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    User queryUsername(String username);
    int addManagerUser(@Param("user") User user);
}