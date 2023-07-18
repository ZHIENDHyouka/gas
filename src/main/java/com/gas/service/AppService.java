package com.gas.service;

import com.gas.entity.*;
import com.gas.mapper.*;
import com.gas.utils.DateTimeUtil;
import com.gas.utils.Md5Util;
import javafx.scene.input.Mnemonic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
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
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private GasMapper gasMapper;

    public ResultVO appLogin(Manager manager) {
        String username = manager.getUsername();
        ;
        String password = Md5Util.getMd5(manager.getPassword());
        int code = 0;
        //查询普通管理员
        Manager checkInfo = managerMapper.queryUsername(username);
        if (checkInfo != null) {
            if (checkInfo.getAccountStatus() == 0) {
                new ResultVO(code, null, "账号禁用");
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
                return new ResultVO(1, null, "注册成功!");
            }
            return new ResultVO(0, null, "注册失败!");
        } else {
            return new ResultVO(0, null, "用户名存在!");
        }
    }

    public ResultVO getDeviceAllInfo() {
        String datetime = DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATEFORMAT) + " 00:00:00";
        List<Map<String, Object>> maps = excessGasMapper.queryDeviceAlarmByStart(datetime);
        List<Device> allDeviceInfo = deviceMapper.getAllDeviceInfo();
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        String msg = "数据获取失败!";
        int code = 0;
        if (allDeviceInfo.size() > 0) {
            if (maps.size() > 0) {
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
                code = 1;
            } else {
                for (Device d : allDeviceInfo) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("deviceName", d.getDeviceName());
                    map.put("status", d.getDeviceStatus() == 1 ? true : false);
                    map.put("alarmCount", 0);
                    result.add(map);
                }
                msg = "报警信息获取失败!";
            }
        }
        return new ResultVO(code, result, msg);
    }

    public ResultVO getGasNameAndNewData() {
        List<Gas> gases = gasMapper.queryAllInfo();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        for (Gas gas : gases) {
            String name = null;
            String dataColumn = "";
            String id = "";
            if ("温度".equals(gas.getName())) {
                dataColumn = "t_data";
                id = "t_id";
            } else if ("湿度".equals(gas.getName())) {
                dataColumn = "h_data";
                id = "h_id";
            } else {
                dataColumn = "g_data";
                id = "g_id";
                name = gas.getName();
            }
            List<Map<String, Object>> list = statisticMapper.queryNewGasData(gas.getTableName(), name, id, dataColumn);
            Map<String, Object> map = list.get(0);
            String data = decimalFormat.format(map.get("data"));
            map.put("data",data);
            map.put("name", gas.getName());
            maps.add(map);
        }
        return new ResultVO(1, maps, "");
    }

    public ResultVO getStatisticInitData(String name) {
        Gas gas = gasMapper.queryGasDBTable(name);
        String gasName = null;
//        String now = DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT);
        String now = "2023-07-11 15:43:29";
        long timeStamp = DateTimeUtil.getStringTimeStamp(now, DateTimeUtil.DATETIMEFORMAT);
        timeStamp -= 1000 * 60 * 60 * 12;
        String start = DateTimeUtil.timeStampTransformString(timeStamp, DateTimeUtil.DATETIMEFORMAT);
        if (!("温度".equals(name) || "湿度".equals(name))) {
            gasName = name;
        }
        String dataColumn = "";
        String indateColumn = "";
        if ("温度".equals(name)) {
            dataColumn = "t_data";
            indateColumn = "t_indate";
        } else if ("湿度".equals(name)) {
            dataColumn = "h_data";
            indateColumn = "h_indate";
        } else {
            dataColumn = "g_data";
            indateColumn = "g_indate";
        }
        String startInterval = DateTimeUtil.addTimeStamp(start, 1000 * 60 * 60);
        List<Map<String, Object>> maps = statisticMapper.queryGasDate(gas.getTableName(), start, now, gasName, dataColumn, indateColumn);
        ArrayList<Double> calcList = new ArrayList<>();
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("0.0000");
        for (Map<String, Object> map : maps) {
            String datetime = map.get(indateColumn).toString();
            Double data = Double.parseDouble(map.get(dataColumn).toString());
            if (datetime.compareTo(startInterval) >= 0) {
                startInterval = DateTimeUtil.addTimeStamp(start, 1000 * 60 * 60);
                HashMap<String, Object> hoursData = new HashMap<>();
                Double avg = calcList.size() > 0 ? getGasListAverage(calcList) : 0.0;
                avg = Double.parseDouble(decimalFormat.format(avg));
                hoursData.put("data", avg);
                hoursData.put("date", start);
                start = DateTimeUtil.addTimeStamp(start, 1000 * 60 * 60);
                calcList.clear();
                result.add(hoursData);
            }
            calcList.add(data);
        }
        return new ResultVO(1, result, "");
    }

    //求平均数
    private Double getGasListAverage(List<Double> dataList) {
        return dataList.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    }

}
