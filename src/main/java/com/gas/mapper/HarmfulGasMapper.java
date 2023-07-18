package com.gas.mapper;

import com.gas.entity.HarmfulGas;
import com.gas.entity.Humidity;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface HarmfulGasMapper {
    int addHarmfulGasDateTest(@Param("gas")HarmfulGas gas);

    int addHarmfulGasData(@Param("gas")HarmfulGas gas);

    List<HarmfulGas> queryConditionData(@Param("sdateTime")String startDateTime,
                                        @Param("edateTime")String endDateTime,
                                        @Param("deviceId")String deviceId,
                                        @Param("gas")String gas);
    @MapKey("id")
    List<Map<String ,Object>> queryRealTimeHarmfulGas(@Param("start")String start,
                                                      @Param("end")String end,
                                                      @Param("deviceId")String deviceId);
    @MapKey("g_name")
    List<Map<String,Object>> queryHarmfulGasAvgData(@Param("start")String start,@Param("end")String end);

    List<Humidity> queryNewData(@Param("name")String name);
}
