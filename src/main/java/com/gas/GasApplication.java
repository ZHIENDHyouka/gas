package com.gas;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gas.mapper")
public class GasApplication {
    public static void main(String[] args) {
        SpringApplication.run(GasApplication.class, args);
    }
}
