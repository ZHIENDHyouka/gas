package com.gas.mapper;

import com.gas.entity.Humidity;
import com.gas.entity.Temperature;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HumidityMapper {
    int addHumidityDateTest(@Param("humidity") Humidity humidity);

    List<Humidity> queryConditionData(@Param("sdateTime")String startDateTime, @Param("edateTime")String endDateTime, @Param("deviceId")String deviceId);
}
