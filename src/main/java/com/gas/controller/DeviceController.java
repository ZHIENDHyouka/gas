package com.gas.controller;

import com.gas.entity.ResultVO;
import com.gas.service.DeivceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
