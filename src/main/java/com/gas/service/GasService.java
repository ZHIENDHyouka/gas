package com.gas.service;

import com.gas.entity.HarmfulGas;
import com.gas.entity.Humidity;
import com.gas.entity.ResultVO;
import com.gas.entity.Temperature;
import com.gas.mapper.HarmfulGasMapper;
import com.gas.mapper.HumidityMapper;
import com.gas.mapper.TemperatureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GasService {
    @Autowired
    private TemperatureMapper temperatureMapper;
    @Autowired
    private HumidityMapper humidityMapper;
    @Autowired
    private HarmfulGasMapper harmfulGasMapper;
    public ResultVO getConditionTableData(Map<String,Object> condition){
        //获取封装数据
        String gasName = condition.get("gas").toString();
        List datetime = condition.get("datetime")==""?null:(List)condition.get("datetime") ;
        String device = condition.get("device").toString();
        String endDateTime = "";
        String startDateTime = "";
        if (datetime!=null) {
            startDateTime = datetime.get(0).toString();
            endDateTime = datetime.get(1).toString();
        }
        //创建返回结果集
        HashMap<String, Object> result = new HashMap<String, Object>();
        if ("温度".equals(gasName)){
            List<Temperature> dataList = temperatureMapper.queryConditionData(startDateTime, endDateTime, device);
            List<Map<String, Object>> temperatureHeadList = getTemperatureHeadList();
            result.put("dataList",dataList);
            result.put("headList",temperatureHeadList);
        }else if ("湿度".equals(gasName)){
            List<Humidity> dataList = humidityMapper.queryConditionData(startDateTime, endDateTime, device);
            List<Map<String, Object>> headList = getHumidityHeadList();
            result.put("headList",headList);
            result.put("dataList",dataList);
        }else{
            if ("全部".equals(gasName)){
                gasName = "";
            }
            List<HarmfulGas> dataList = harmfulGasMapper.queryConditionData(startDateTime, endDateTime, device,gasName);
            List<Map<String, Object>> headList = getHarmfulGasHeadList();
            result.put("headList",headList);
            result.put("dataList",dataList);
        }
        return new ResultVO(1,result,"");
    }

    public ResultVO getAllTemperatureList(){
        //查询所有数据
        List<Temperature> dataList = temperatureMapper.queryAllTemperatureInfo();
        //获取表头List
        List<Map<String, Object>> headList = getTemperatureHeadList();
        //封装数据 封装格式要固定!!!
        HashMap<String, Object> result = new HashMap<>();
        result.put("dataList",dataList);
        result.put("headList",headList);
        return new ResultVO(1,result,null);
    }

    private List<Map<String,Object>> getHarmfulGasHeadList(){
        ArrayList<Map<String, Object>> headList = new ArrayList<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("proper","id");
        map1.put("label","记录编号");
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("proper","gasName");
        map2.put("label","气体名称");
        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("proper","gasData");
        map3.put("label","气体浓度");
        HashMap<String, Object> map4 = new HashMap<>();
        map4.put("proper","indate");
        map4.put("label","记录时间");
        HashMap<String, Object> map5 = new HashMap<>();
        map5.put("proper","deviceId");
        map5.put("label","记录设备编号");
        headList.add(map1);
        headList.add(map2);
        headList.add(map3);
        headList.add(map4);
        headList.add(map5);
        return headList;
    }

    private List<Map<String,Object>>getHumidityHeadList(){
        ArrayList<Map<String, Object>> headList = new ArrayList<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("proper","id");
        map1.put("label","记录编号");
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("proper","humidityDate");
        map2.put("label","空气湿度");
        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("proper","indate");
        map3.put("label","记录时间");
        HashMap<String, Object> map4 = new HashMap<>();
        map4.put("proper","deviceId");
        map4.put("label","记录设备编号");
        headList.add(map1);
        headList.add(map2);
        headList.add(map3);
        headList.add(map4);
        return headList;
    }

    private List<Map<String,Object>> getTemperatureHeadList(){
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