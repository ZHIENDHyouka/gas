package com.gas.controller;

import com.gas.entity.ResultVO;
import com.gas.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temperature")
@CrossOrigin
public class TemperatureController {
    @Autowired
    private TemperatureService temperatureService;

    @GetMapping("/getAllTemperatureList")
    public ResultVO getAllTemperatureList(){
        ResultVO allTemperatureList = temperatureService.getAllTemperatureList();
        return allTemperatureList;
    }

}
