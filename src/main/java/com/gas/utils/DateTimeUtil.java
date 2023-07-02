package com.gas.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {
    public static final String  DATETIMEFORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String  DATEFORMAT = "yyyy-MM-dd";
    public static final String  TIMEFORMAT = "HH:mm:ss";

    /**
     * 获取当前时间对应格式化的时间字符串格式数据
     * @param format
     * @return
     */
    public static String getNowFormatDateTimeString(String format){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }

    public static String getLocalDateTimeFormat(LocalDateTime dateTime,String format){
        String Format = dateTime.format(DateTimeFormatter.ofPattern(format));
        return Format;
    }

    public static LocalDateTime stringTransformLocalDateTime(String datetime,String format){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        LocalDateTime ldt = LocalDateTime.parse(datetime,df);
        return ldt;
    }
}
