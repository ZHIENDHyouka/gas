package com.gas.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriticalValue {
    private int id;
    private String name ;
    private String lowerLimit;
    private String upperLimit;
    private String symbol;
}
