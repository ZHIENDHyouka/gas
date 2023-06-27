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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.gas.utils.DateTimeUtil.DATETIMEFORMAT;

@Service
@Transactional
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
        if (manager != null) {
            if (Md5Util.getMd5(user.getPassword()).equals(manager.getPassword())) {
                //密码正确
                msg = "登陆成功!";
                code = 1;
                username = manager.getUsername();
            } else {
                //密码错误
                msg = "密码错误!";
            }
        } else {
            msg = "账号不存在!";
        }
        ResultVO resultVO = new ResultVO(code, username, msg);
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
            String indate = DateTimeUtil.getNowFormatDateTimeString(DATETIMEFORMAT);
            ManagerReview managerReview = new ManagerReview(0, username, md5Password, indate, 0, "");
            code = reviewMapper.addReviewInfo(managerReview);
            if (code > 0) msg = "申请成功!";
            else msg = "申请失败，好像有bug捏!";
        } else {
            msg = "用户名已经存在请更换用户名再注册申请!";
        }
        return new ResultVO(code, null, msg);
    }

    public ResultVO getAllManagerReviewList() {
        ResultVO resultVO = new ResultVO(0, null, "");
        List<ManagerReview> managerReviewList = reviewMapper.getAllManagerReviewList();
        if (managerReviewList != null) {
            resultVO.setCode(1);
            resultVO.setMsg("查询成功");
            resultVO.setData(managerReviewList);
        } else {
            resultVO.setMsg("查询失败，请联系管理员");
        }
        return resultVO;
    }

    public ResultVO addManagerReview(String id) {
        ResultVO resultVO = new ResultVO(0, null, "");
        String date = DateTimeUtil.getNowFormatDateTimeString(DATETIMEFORMAT);
        String status = "1";
        int success = reviewMapper.updateById(id, date, status);
        if (success == 0) {
            throw new RuntimeException("插入操作错误");
        }
        ManagerReview managerReview = reviewMapper.selectById(id);
        Manager manager = new Manager();
        manager.setManagerId(managerReview.getId());
        manager.setUsername(managerReview.getUsername());
        manager.setPassword(managerReview.getPassword());
        manager.setManagerLevel(2);
        int i = managerMapper.addManagerUser(manager);
        if (i == 1) {
            resultVO.setCode(1);
        }
        return resultVO;
    }

    public ResultVO refuseManagerReview(String id) {
        ResultVO resultVO = new ResultVO(0, null, "");
        String date = DateTimeUtil.getNowFormatDateTimeString(DATETIMEFORMAT);
        String status = "-1";
        int success = reviewMapper.updateById(id, date, status);
        if (success == 0) {
            throw new RuntimeException("删除操作错误");
        }
        resultVO.setCode(1);
        return resultVO;
    }
}
