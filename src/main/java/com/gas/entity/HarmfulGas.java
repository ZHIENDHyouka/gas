package com.gas.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HarmfulGas {
    //气体类
    @ExcelProperty("记录编号")
    private int id;
    @ExcelProperty("气体名称")
    private String gasName;
    @ExcelProperty("气体浓度")
    private double gasData;
    @ExcelProperty("记录时间")
    private String indate;
    @ExcelProperty("记录设备编号")
    private String deviceId;
}
