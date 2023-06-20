package com.gas.mapper;

import com.gas.entity.Temperature;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TemperatureMapper {
     int addTemperatureDateTest(@Param("temperature")Temperature temperature);
}
