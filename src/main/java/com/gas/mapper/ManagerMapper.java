package com.gas.mapper;

import com.gas.entity.Manager;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerMapper {
    Manager queryUsername(@Param("username") String username);

    int addManagerUser(@Param("manager") Manager user);

    List<Manager> queryAllManager();

    int updateAccountStatusById(@Param("id")int id,@Param("status")int status);
}