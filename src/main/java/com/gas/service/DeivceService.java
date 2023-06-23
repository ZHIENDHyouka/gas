package com.gas.service;

import com.gas.entity.Device;
import com.gas.entity.ResultVO;
import com.gas.mapper.DeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.ObjectName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeivceService {
    @Autowired
    private DeviceMapper deviceMapper;

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

}
