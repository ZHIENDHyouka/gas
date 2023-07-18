package com.gas.controller;

import com.gas.entity.Manager;
import com.gas.entity.ResultVO;
import com.gas.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/app")
@CrossOrigin
public class AppController {
    @Autowired
    private AppService appService;

    @PostMapping("/appLogin")
    public ResultVO appLogin(@RequestBody Manager manager){
        ResultVO resultVO = appService.appLogin(manager);
        return  resultVO;
    }

    @PostMapping("/appRegister")
    public ResultVO appRegister(@RequestBody Manager manager){
        ResultVO resultVO = appService.appRegister(manager);
        return resultVO;
    }

    @GetMapping("/getDeviceAllInfo")
    public ResultVO getDeviceAllInfo(){
        ResultVO deviceAllInfo = appService.getDeviceAllInfo();
        return deviceAllInfo;
    }

    @GetMapping("/getGasNameAndNewData")
    public ResultVO getGasNameAndNewData(){
        return appService.getGasNameAndNewData();
    }

    @PostMapping("/getStatisticInitData")
    public ResultVO getStatisticInitData(@RequestBody Map<String,Object> map){
        String name = map.get("name").toString();
        return appService.getStatisticInitData(name);
    }
}
