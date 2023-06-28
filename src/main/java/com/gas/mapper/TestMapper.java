package com.gas.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TestMapper {
    int updateById(@Param("id")int id);
}
