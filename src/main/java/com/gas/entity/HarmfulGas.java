package com.gas.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HarmfulGas {
    private int id;
    private String gasName;
    private double gasData;
    private String indate;
    private String deviceId;
}
