package com.gas.service;

import com.gas.entity.ResultVO;
import com.gas.entity.Temperature;
import com.gas.mapper.TemperatureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TemperatureService {
    @Autowired
    private TemperatureMapper temperatureMapper;

    public ResultVO getAllTemperatureList(){
        //查询所有数据
        List<Temperature> dataList = temperatureMapper.queryAllTemperatureInfo();
        //获取表头List
        List<Map<String, Object>> headList = getHeadList();
        //封装数据 封装格式要固定!!!
        HashMap<String, Object> result = new HashMap<>();
        result.put("dataList",dataList);
        result.put("headList",headList);
        return new ResultVO(1,result,null);
    }


    private List<Map<String,Object>> getHeadList(){
        List<Map<String, Object>> headList = new ArrayList<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("proper","id");
        map1.put("label","记录编号");
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("proper","temperatureDate");
        map2.put("label","温度数据(℃)");
        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("proper","indate");
        map3.put("label","记录时间");
        HashMap<String, Object> map4 = new HashMap<>();
        map4.put("proper","deivceId");
        map4.put("label","记录设备编号");
        headList.add(map1);
        headList.add(map2);
        headList.add(map3);
        headList.add(map4);
        return headList;
    }
}
