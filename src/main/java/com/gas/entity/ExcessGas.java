package com.gas.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcessGas {

    @ExcelProperty("记录编号")
    private int id;
    @ExcelProperty("气体名称")
    private String gasName;
    @ExcelProperty("数值")
    private String value;
    @ExcelProperty("类型")
    private String gasType;
    @ExcelProperty("记录时间")
    private String indatetime;
    @ExcelProperty("记录设备编号")
    private String deviceId;
}
