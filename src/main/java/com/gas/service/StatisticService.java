package com.gas.service;

import com.gas.entity.Gas;
import com.gas.entity.ResultVO;
import com.gas.mapper.GasMapper;
import org.apache.poi.ss.formula.functions.T;
import org.ehcache.shadow.org.terracotta.statistics.StatisticMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatisticService {
    @Autowired
    private GasMapper gasMapper;

    public ResultVO queryGasNameList(){
        List<Gas> gases = gasMapper.queryAllGasName();
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        if (gases.size()>0) {
            //封装数据
            for (int i = 0; i < gases.size(); i++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("index", i + "");
                map.put("name", gases.get(i).getName());
                result.add(map);
            }
            return new ResultVO(1,result,"数据获取成功");
        }
        return new ResultVO(0,result,"没有气体入库或者出现bug");
    }

    public ResultVO getStatisticData(Map param){
        String gasName = param.get("name").toString();//气体名称
        String satisticValue = param.get("satisticValue").toString();//统计方式
        String datetime = param.get("datetime").toString();//统计时间跨度
        return null;
    }

    //求平均数
    private Double getGasListAverage(List<Double> dataList){
        return dataList.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    }

    //求中位数
    private  Double getGasListMedian(List<Double> dataList){
        //排序
        dataList.sort(Comparator.naturalOrder());
        int size = dataList.size();
        if (size%2==0) return dataList.get(size/2);
        return (dataList.get(size/2)+dataList.get(size/2+1))/2;
    }
}
