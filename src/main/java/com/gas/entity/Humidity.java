package com.gas.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Objects;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Humidity {
    //湿度类
    @ExcelProperty("记录编号")
    private int id;
    @ExcelProperty("空气湿度")
    private double humidityDate;
    @ExcelProperty("记录时间")
    private String indate;
    @ExcelProperty("记录设备编号")
    private String deviceId;

}
