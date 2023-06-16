package com.gas.service;

import com.gas.entity.ResultVO;
import com.gas.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public ResultVO managerLogin(User user){
        System.out.println(user);
        return null;
    }
}
