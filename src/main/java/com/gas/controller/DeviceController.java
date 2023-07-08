package com.gas.controller;

import com.gas.entity.ResultVO;
import com.gas.service.DeivceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/device")
@CrossOrigin
public class DeviceController {
    @Autowired
    private DeivceService deivceService;

    @GetMapping("/getDeviceNameList")
    public ResultVO getDeviceNameList(){
        return deivceService.getDeviceNameList();
    }

    @GetMapping("/getDeviceRunNumber")
    public ResultVO getDeviceRunNumber(){
        ResultVO deviceRunNumber = deivceService.getDeviceRunNumber();
        return deviceRunNumber;
    }

    @GetMapping("/getAllDeviceInfo")
    public ResultVO getAllDeviceInfo(){
        ResultVO allDevice = deivceService.getAllDeviceInfo();
        return allDevice;
    }

    @PostMapping("/updateDeviceState")
    public ResultVO updateDeviceState(@RequestBody Map<String,Integer> message){
        ResultVO result =deivceService.updateDeviceState(message);
        return result;
    }
}
