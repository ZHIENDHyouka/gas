package com.gas.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcessGas {
    private int id;
    private String gasName;
    private String gasType;
    private String indatetime;
    private String value;
    private String deviceId;
}
