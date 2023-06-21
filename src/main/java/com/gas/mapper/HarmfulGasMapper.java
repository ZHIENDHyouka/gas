package com.gas.mapper;

import com.gas.entity.HarmfulGas;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HarmfulGasMapper {
    int addHarmfulGasDateTest(@Param("gas")HarmfulGas gas);
}
