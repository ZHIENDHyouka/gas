package com.gas.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ( feedback)实体类
 *
 * @author makejava
 * @since 2023-07-09 17:55:58
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    /**
     * id
     */
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 提交时间
     */
    private String submitTime;
    /**
     * 问题描述
     */
    private String problemDescribe;
    /**
     * 0 未解决 1已解决
     */
    private Integer status;


}

