package com.gas.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResultVO {
    private int code; //1成功 0失败
    private Object data;//数据
    private String msg;//提示信息 爱写不写都行
}
