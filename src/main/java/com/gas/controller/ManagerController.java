package com.gas.controller;

import com.gas.entity.ResultVO;
import com.gas.entity.Manager;
import com.gas.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/manager")
@CrossOrigin
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    @PostMapping("/managerLogin")
    public ResultVO managerLogin(@RequestBody Manager user){
        ResultVO resultVO = managerService.managerLogin(user);
        return resultVO;
    }

    @PostMapping("/managerRegister")
    public ResultVO managerRegister(@RequestBody Manager user){
        ResultVO resultVO = managerService.managerRegister(user);
        return resultVO;
    }

    @GetMapping("/getAllManagerInfo")
    public ResultVO getAllManagerInfo(){
        ResultVO allManagerInfo = managerService.getAllManagerInfo();
        return allManagerInfo;
    }

    @GetMapping("/changeManagerAcountStatus")
    public ResultVO changeManagerAcountStatus(@RequestParam("id")int id,@RequestParam("status")int status){
        ResultVO resultVO = managerService.changeManagerAcountStatus(id, status);
        return resultVO;
    }

    @PostMapping("/searchNameOrStatusData")
    public ResultVO searchNameOrStatusData(@RequestBody Map<String,Object>searchMap){
        ResultVO resultVO = managerService.searchNameOrStatusData(searchMap);
        return resultVO;
    }

    @GetMapping("/getAllManagerReviewList")
    public ResultVO getAllManagerReviewList(){
        return managerService.getAllManagerReviewList();
    }

    @PostMapping("/addManagerReview")
    public ResultVO addManagerReview(@RequestBody Map<String,String> idMap){
        return managerService.addManagerReview(idMap.get("id"));
    }

    @PostMapping("/refuseManagerReview")
    public ResultVO refuseManagerReview(@RequestBody Map<String,String> idMap){
        return managerService.refuseManagerReview(idMap.get("id"));
    }



}
