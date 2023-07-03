package com.gas.mapper;

import com.gas.entity.ExcessGas;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExcessGasMapper {
    int insertAlarmGasInfo(@Param("gas") ExcessGas excessGas);

    int queryRealTimeList(@Param("nowDateTime")String now,@Param("beforeDateTime")String before);
}
