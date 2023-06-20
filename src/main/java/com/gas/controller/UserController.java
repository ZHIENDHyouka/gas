package com.gas.controller;

import com.gas.entity.ResultVO;
import com.gas.entity.Manager;
import com.gas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/managerLogin")
    public ResultVO managerLogin(@RequestBody Manager user){
        ResultVO resultVO = userService.managerLogin(user);
        return resultVO;
    }

    @PostMapping("/managerRegister")
    public ResultVO managerRegister(@RequestBody Manager user){
        ResultVO resultVO = userService.managerRegister(user);
        return resultVO;
    }
}
