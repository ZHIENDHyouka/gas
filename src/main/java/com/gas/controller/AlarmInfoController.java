package com.gas.controller;

import com.gas.entity.ResultVO;
import com.gas.service.AlarmInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    @GetMapping("/getDayAllAlarmInfo")
    public ResultVO getDayAllAlarmInfo(){
        return alarmInfoService.getDayAllAlarmInfo();
    }

    @PostMapping("/updateGasAlarmCritical")
    public ResultVO updateGasAlarmCritical(@RequestBody Map map){
        ResultVO resultVO = alarmInfoService.updateGasAlarmCritical(map);
        return  resultVO;
    }

    @GetMapping("/queryAllAlarmCriticalInfo")
    public ResultVO queryAllAlarmCriticalInfo(){
       return alarmInfoService.queryAllAlarmCriticalInfo();
    }


}
