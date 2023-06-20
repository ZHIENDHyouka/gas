package com.gas.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manager {
    private int managerId;
    private String username;
    private String password;
    private int managerLevel;
    private int accountStatus;
}
