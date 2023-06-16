package com.gas.utils;

import org.springframework.util.DigestUtils;

public class Md5Util {
    public static String getMd5(String password){
        //MD5加密
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}
