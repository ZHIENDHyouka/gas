package com.gas.controller;

import com.gas.entity.ResultVO;
import com.gas.service.GasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/gas")
@CrossOrigin
public class GasController {
    @Autowired
    private GasService gasService;

    @GetMapping("/getAllTemperatureList")
    public ResultVO getAllTemperatureList(){
        ResultVO allTemperatureList = gasService.getAllTemperatureList();
        return allTemperatureList;
    }

    @PostMapping("/getConditionTableData")
    public ResultVO getConditionTableData(@RequestBody Map<String,Object> condition){
        ResultVO conditionTableData = gasService.getConditionTableData(condition);
        return conditionTableData;
    }

    @PostMapping("/download")
    public void download(@RequestBody Map<String,Object> condition, HttpServletResponse response){
//        ResultVO result = gasService.download(condition,response);
        gasService.download(condition,response);
//        return result;
    }

    @GetMapping("/getHarmfulGasAvgData")
    public ResultVO queryHarmfulGasAvgData(){
        return gasService.queryHarmfulGasAvgData();
    }


}
