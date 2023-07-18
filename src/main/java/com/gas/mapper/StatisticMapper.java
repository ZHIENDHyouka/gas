package com.gas.mapper;

import com.gas.entity.HarmfulGas;
import com.gas.entity.Temperature;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface StatisticMapper {
    @MapKey("id")
    List<Map<String,Object>> queryGasDate(@Param("table")String table,
                                          @Param("start")String start,
                                          @Param("end")String end,
                                          @Param("gasName")String gasName,
                                          @Param("dataColumn")String data,
                                          @Param("indateColumn")String indate);
    @MapKey("id")
    List<Map<String,Object>> queryNewGasData(@Param("table")String tableName,
                                             @Param("gasName")String gasName,
                                             @Param("id") String id,
                                             @Param("dataColumn")String dataColumn);
}
