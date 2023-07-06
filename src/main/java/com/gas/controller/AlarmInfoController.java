package com.gas.controller;

import com.gas.entity.ResultVO;
import com.gas.service.AlarmInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alarm")
@CrossOrigin
public class AlarmInfoController {
    @Autowired
    private AlarmInfoService alarmInfoService;

    @GetMapping("/getRealTimeAlarmDataer")
    public ResultVO getRealTimeAlarmDataer(){
        ResultVO result = alarmInfoService.getRealTimeAlarmDataer();
        return result;
    }
}
