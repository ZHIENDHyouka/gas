package com.gas.mapper;

import com.gas.entity.Humidity;
import com.gas.entity.Temperature;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HumidityMapper {
    int addHumidityDateTest(@Param("humidity") Humidity humidity);
}
