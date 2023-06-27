package com.gas.mapper;

import com.gas.entity.HarmfulGas;
import com.gas.entity.Humidity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HarmfulGasMapper {
    int addHarmfulGasDateTest(@Param("gas")HarmfulGas gas);

    List<HarmfulGas> queryConditionData(@Param("sdateTime")String startDateTime,
                                        @Param("edateTime")String endDateTime,
                                        @Param("deviceId")String deviceId,
                                        @Param("gas")String gas);
}
