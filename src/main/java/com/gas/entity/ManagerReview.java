package com.gas.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerReview {
    private int id;
    private String username;
    private String password;
    private String indate;
    private int status;
    private String outdate;
}
