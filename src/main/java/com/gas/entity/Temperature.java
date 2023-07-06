package com.gas.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Temperature {
    //温度类
    @ExcelProperty("记录编号")
    private int id;
    @ExcelProperty("温度数据(℃)")
    private double temperatureDate;
    @ExcelProperty("记录时间")
    private String indate;
    @ExcelProperty("记录设备编号")
    private String deivceId;

}
