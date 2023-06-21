package com.gas.service;

import com.gas.entity.ManagerReview;
import com.gas.entity.ResultVO;
import com.gas.entity.Manager;
import com.gas.mapper.ManagerMapper;
import com.gas.mapper.ManagerReviewMapper;
import com.gas.utils.DateTimeUtil;
import com.gas.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {
    @Autowired
    private ManagerMapper managerMapper;

    @Autowired
    private ManagerReviewMapper reviewMapper;

    public ResultVO managerLogin(Manager user) {
        Manager manager = managerMapper.queryUsername(user.getUsername());
        String msg = "";
        int code = 0;
        String username = null;
        if (manager!=null){
            if (Md5Util.getMd5(user.getPassword()).equals(manager.getPassword())){
                //密码正确
                msg="登陆成功!";
                code=1;
                username=manager.getUsername();
            }else {
                //密码错误
                msg="密码错误!";
            }
        }else {
            msg="账号不存在!";
        }
        ResultVO resultVO = new ResultVO(code,username,msg);
        return resultVO;
    }

    public ResultVO managerRegister(Manager user) {
        int code = 0;
        String msg = "";
        Manager queryUsername = managerMapper.queryUsername(user.getUsername());
        if (queryUsername == null) {
            //密码加密
            String md5Password = Md5Util.getMd5(user.getPassword());
            String username = user.getUsername();
            String indate = DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT);
            ManagerReview managerReview = new ManagerReview(0, username, md5Password, indate, 0, "");
            code = reviewMapper.addReviewInfo(managerReview);
            if (code > 0) msg = "申请成功!";
            else msg="申请失败，好像有bug捏!";
        } else {
            msg = "用户名已经存在请更换用户名再注册申请!";
        }
        return new ResultVO(code, null, msg);
    }
}
