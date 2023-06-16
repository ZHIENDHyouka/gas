package com.gas.controller;

import com.gas.entity.ResultVO;
import com.gas.entity.User;
import com.gas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/managerLogin")
    public ResultVO managerLogin(@RequestBody User user){
        ResultVO resultVO = userService.managerLogin(user);
        return resultVO;
    }
}
