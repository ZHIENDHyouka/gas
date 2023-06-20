package com.gas.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Temperature {
    //温度类
    private int id;
    private double temperatureDate;
    private String indate;
    private String deivceId;

}
