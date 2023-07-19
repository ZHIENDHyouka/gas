package com.gas.mapper;

import com.gas.entity.CriticalValue;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Repository
public interface CriticalValueMapper {
    int updateAlarmCriticalValue(@Param("lower")String lower,@Param("upper")String upper,@Param("id")int id);
    List<CriticalValue> queryAllCriticalInfo();
}
