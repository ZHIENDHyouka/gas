package com.gas.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RecordDeviceNumberMapper {
    int insertRecordNumberInfo(@Param("number") Integer number, @Param("datetime")String dateTime);

    @MapKey("id")
    List<Map<String,Object>> querySevenHoursData();

}
