package com.gas.mapper;

import com.gas.entity.Device;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceMapper {
    int addDeviceDataTest(@Param("device")Device device);

    List<Device> queryDeviceAllName();

    int queryDeviceRunNumber();

    List<Device> getAllDeviceInfo();

    List<Device> getDeviceInfoByStatus(@Param("status") int status);

    int updateDeviceState(Integer state, Integer serviceId);
}
