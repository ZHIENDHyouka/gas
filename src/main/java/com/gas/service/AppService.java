package com.gas.service;

import com.gas.entity.Device;
import com.gas.entity.ExcessGas;
import com.gas.entity.Manager;
import com.gas.entity.ResultVO;
import com.gas.mapper.DeviceMapper;
import com.gas.mapper.ExcessGasMapper;
import com.gas.mapper.ManagerMapper;
import com.gas.utils.DateTimeUtil;
import com.gas.utils.Md5Util;
import javafx.scene.input.Mnemonic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppService {
    @Autowired
    private ManagerMapper managerMapper;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private ExcessGasMapper excessGasMapper;

    public ResultVO appLogin(Manager manager) {
        String username = manager.getUsername();
        ;
        String password = Md5Util.getMd5(manager.getPassword());
        int code = 0;
        //查询普通管理员
        Manager checkInfo = managerMapper.queryUsername(username);
        if (checkInfo != null) {
            if (checkInfo.getAccountStatus()==0){
                new ResultVO(code,null,"账号禁用");
            }
            if (!password.equals(checkInfo.getPassword())) {
                return new ResultVO(code, null, "密码错误!");
            }
            if (checkInfo.getManagerLevel() != 2) {
                return new ResultVO(code, null, "无法登陆!");
            }
            code = 1;
            checkInfo.setPassword("");
            return new ResultVO(code, checkInfo, "登陆成功!");

        } else {
            return new ResultVO(code, null, "用户不存在,请注册!");
        }
    }

    public ResultVO appRegister(Manager manager) {
        String username = manager.getUsername();
        String password = Md5Util.getMd5(manager.getPassword());
        Manager checkInfo = managerMapper.queryUsername(username);
        if (checkInfo == null) {
            int i = managerMapper.addManagerUser(new Manager(0, username, password, 2, 0));
            if (i > 0) {
                return new ResultVO(1,null,"注册成功!");
            }
            return new ResultVO(0,null,"注册失败!");
        } else {
            return new ResultVO(0, null, "用户名存在!");
        }
    }

    public ResultVO getDeviceAllInfo(){
        String datetime = DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATEFORMAT)+" 00:00:00";
        List<Map<String, Object>> maps = excessGasMapper.queryDeviceAlarmByStart(datetime);
        List<Device> allDeviceInfo = deviceMapper.getAllDeviceInfo();
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        String msg= "数据获取失败!";
        int code = 0;
        if (allDeviceInfo.size()>0) {
            if (maps.size()>0) {
                int count = 0;
                for (Device d : allDeviceInfo) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("deviceName", d.getDeviceName());
                    map.put("status", d.getDeviceStatus() == 1 ? true : false);
                    String deviceName = maps.get(count).get("deviceName").toString();
                    if (d.getDeviceName().equals(deviceName)) {
                        map.put("alarmCount", maps.get(count).get("number"));
                        count++;
                    } else {
                        map.put("alarmCount", 0);
                    }
                    result.add(map);
                }
                code=1;
            }else {
                for (Device d : allDeviceInfo) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("deviceName", d.getDeviceName());
                    map.put("status", d.getDeviceStatus() == 1 ? true : false);
                    map.put("alarmCount", 0);
                    result.add(map);
                }
                msg="报警信息获取失败!";
            }
        }
        return new ResultVO(code,result,msg);
    }
}
