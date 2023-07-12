package com.gas.controller;

import com.gas.entity.ResultVO;
import com.gas.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/statistic")
@CrossOrigin
public class StatisticController {
    @Autowired
    private StatisticService statisticService;

    @GetMapping("/getGasNameList")
    public ResultVO queryGasNameList(){
        ResultVO resultVO = statisticService.queryGasNameList();
        return resultVO;
    }

    @PostMapping("/getStatisticData")
    public ResultVO getStatisticData(@RequestBody Map param){
        ResultVO statisticData = statisticService.getStatisticData(param);
        return statisticData;
    }
}
