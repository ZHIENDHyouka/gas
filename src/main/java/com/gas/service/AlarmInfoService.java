package com.gas.service;

import com.gas.common.SpringContext;
import com.gas.common.client.AmqpClient;
import com.gas.entity.CriticalValue;
import com.gas.entity.ExcessGas;
import com.gas.entity.ResultVO;
import com.gas.mapper.CriticalValueMapper;
import com.gas.mapper.ExcessGasMapper;
import com.gas.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlarmInfoService {
    @Autowired
    private ExcessGasMapper excessGasMapper;
    @Autowired
    private CriticalValueMapper criticalValueMapper;

    public ResultVO getRealTimeAlarmDataer() {
        List<Map<String, Object>> lists = excessGasMapper.queryRealTimeAlarmDataList();
        String msg = "暂无报警数据";
        int code = 0;
        HashMap<String, Object> result = null;
        ArrayList<Object> dateList = new ArrayList<>();
        ArrayList<Object> valueList = new ArrayList<>();
        if (lists.size() > 0) {
            code = 1;
            msg = "";
            Collections.reverse(lists);
            for (Map m : lists) {
                dateList.add(m.get("indatetime").toString().split(" ")[1]);
                valueList.add(m.get("number"));
            }
            result = new HashMap<String, Object>();
            result.put("dateList", dateList);
            result.put("valueList", valueList);
        }
        return new ResultVO(code, result, msg);
    }

    public ResultVO getDayAllAlarmInfo() {
        String now = DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT);
        String date = now.split(" ")[0];
        date += " 00:00:00";
        List<ExcessGas> excessGases = excessGasMapper.queryDayAllAlarmData(date);
        return new ResultVO(1, excessGases, "");
    }

    public ResultVO updateGasAlarmCritical(Map<String, Object> map) {
        Integer id = (Integer) map.get("id");
        String lowerLimit = map.get("lowerLimit").toString();
        String upperLimit = map.get("upperLimit").toString();
        int i = criticalValueMapper.updateAlarmCriticalValue(lowerLimit, upperLimit, id);
        if (i > 0) {
            //更新接收云平台数据的判断标准
            synchronized (this) {
                try {
                    List<CriticalValue> criticalValues = criticalValueMapper.queryAllCriticalInfo();
                    SpringContext.replaceBean("criticalValueList", criticalValues);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new ResultVO(1, null, "修改成功!");
        }
        return new ResultVO(0, null, "修改失败!");
    }

    public ResultVO queryAllAlarmCriticalInfo() {
        List<CriticalValue> criticalValues = criticalValueMapper.queryAllCriticalInfo();
        return new ResultVO(1, criticalValues, "");
    }
}
