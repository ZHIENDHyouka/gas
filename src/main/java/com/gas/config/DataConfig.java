package com.gas.config;

import com.gas.entity.CriticalValue;
import com.gas.entity.Device;
import com.gas.mapper.CriticalValueMapper;
import com.gas.mapper.DeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Configuration
public class DataConfig {
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private CriticalValueMapper criticalValueMapper;
    @Bean(name = "deviceNameList")
    public List<Device> getDeviceNameList(){
        //使用CopyOnWriteArrayList避免并发错误
        CopyOnWriteArrayList<Device> deviceNameList = new CopyOnWriteArrayList(deviceMapper.queryDeviceAllName().toArray());
        return  deviceNameList ;
    }
    @Bean(name="criticalValueList")
    public List<CriticalValue> getCriticalValueList(){
        CopyOnWriteArrayList criticalValueList = new CopyOnWriteArrayList(criticalValueMapper.queryAllCriticalInfo());
        return criticalValueList;
    }
}
