package com.gas.mapper;

import com.gas.entity.ExcessGas;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ExcessGasMapper {
    int insertAlarmGasInfo(@Param("gas") ExcessGas excessGas);

    int queryRealTimeNumber(@Param("nowDateTime")String now,@Param("beforeDateTime")String before);

    List<ExcessGas> queryConditionData(String startDateTime, String endDateTime, String deviceId);

    @MapKey("id")
    List<Map<String,Object>> queryRealTimeAlarmDataList();

    int queryDayAllAlarmNumber(@Param("datetime")String datetime);

    List<ExcessGas> queryDayAllAlarmData(@Param("datetime")String datetime);

    @MapKey("e_id")
    List<Map<String,Object>> queryDeviceAlarmByStart(@Param("datetime")String datetime);
}
