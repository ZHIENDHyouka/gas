package com.gas.service;

import com.gas.entity.ResultVO;
import com.gas.entity.Manager;
import com.gas.mapper.UserMapper;
import com.gas.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {
    @Autowired
    private UserMapper userMapper;

    public ResultVO managerLogin(Manager user) {
        System.out.println(user);
        return null;
    }

    public ResultVO managerRegister(Manager user) {
        System.out.println(user);
        int code = 0;
        String msg = "";
        Manager queryUsername = userMapper.queryUsername(user.getUsername());
        if (queryUsername == null) {
            //密码加密
            user.setPassword(Md5Util.getMd5(user.getPassword()));
            code = userMapper.addManagerUser(user);
            if (code > 0) msg = "注册成功!";
            else msg="注册失败，好像有bug捏!";
        } else {
            msg = "用户名已经存在请更换用户名再注册!";
        }
        return new ResultVO(code, null, msg);
    }
}
