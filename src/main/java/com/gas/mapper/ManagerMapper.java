package com.gas.mapper;

import com.gas.entity.Manager;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerMapper {
    Manager queryUsername(String username);
    int addManagerUser(@Param("manager") Manager user);
}