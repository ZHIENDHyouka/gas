package com.gas.controller;

import com.gas.entity.ResultVO;
import com.gas.entity.Manager;
import com.gas.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
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
}
