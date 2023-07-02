package com.gas.service;

import com.gas.entity.Device;
import com.gas.entity.ResultVO;
import com.gas.mapper.DeviceMapper;
import com.gas.mapper.RecordDeviceNumberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DeivceService {
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private RecordDeviceNumberMapper recordDeviceNumberMapper;

    public ResultVO getDeviceNameList(){
        List<Device> devices = deviceMapper.queryDeviceAllName();
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        devices.stream().forEach(item->{
            HashMap<String, Object> map = new HashMap<>();
            map.put("value",item.getDeviceName());
            map.put("label",item.getDeviceName());
            maps.add(map);
        });
        return new ResultVO(1,maps,null);
    }

    public ResultVO getDeviceRunNumber(){
        List<Map<String, Object>> maps = recordDeviceNumberMapper.querySevenHoursData();
        ArrayList<Integer> runNumberList = new ArrayList<>();
        ArrayList<String> datatimeList = new ArrayList<>();
        String msg = "";
        int code = 0;
        if (maps.size()>0) {
            code=1;
            for (Map map : maps) {
                Integer rumNumber = (Integer) map.get("run_device_number");
                String datetime = map.get("record_datetime").toString().split(" ")[1];
                runNumberList.add(rumNumber);
                datatimeList.add(datetime);
            }
        }else {
            msg="暂无7小时内的数据!";
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("y",runNumberList);
        result.put("x",datatimeList);
        return new ResultVO(code,result,msg);
    }


}
