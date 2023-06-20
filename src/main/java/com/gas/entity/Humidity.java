package com.gas.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Humidity {
    //湿度类
    private int id;
    private double humidityDate;
    private String indate;
    private String deviceId;
}
