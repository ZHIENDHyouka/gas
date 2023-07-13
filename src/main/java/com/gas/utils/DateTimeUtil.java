package com.gas.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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

    /**
     * localdate类型转String
     * @param dateTime
     * @param format
     * @return
     */
    public static String getLocalDateTimeFormat(LocalDateTime dateTime,String format){
        String Format = dateTime.format(DateTimeFormatter.ofPattern(format));
        return Format;
    }

    /**
     * String转localdate类型
     * @param datetime
     * @param format
     * @return
     */
    public static LocalDateTime stringTransformLocalDateTime(String datetime,String format){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        LocalDateTime ldt = LocalDateTime.parse(datetime,df);
        return ldt;
    }

    /**
     * 获取传入字符串时间的后n天的时间字符串
     * @param n
     * @param datetime
     * @param datetimeFormat
     * @param transFormat
     * @return
     */
    public static String getAddFormatDay(int n,String datetime,String datetimeFormat,String transFormat){
        long l = stringTransformLocalDateTime(datetime,datetimeFormat).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        l+=n*1000*60*60*24;
        Instant instant = Instant.ofEpochMilli(l);
        return getLocalDateTimeFormat(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()),transFormat);
    }

    /**
     * string类型时间的格式转换
     * @param datetime
     * @param format
     * @param transFormat
     * @return
     */
    public static String stringDateTimeFormat(String datetime,String format,String transFormat){
        LocalDateTime localDateTime = stringTransformLocalDateTime(datetime, format);
        return getLocalDateTimeFormat(localDateTime,transFormat);
    }

    /**
     * string时间转时间戳
     * @param datetime
     * @param format
     * @return
     */
    public static long getStringTimeStamp(String datetime,String format){
        return stringTransformLocalDateTime(datetime,format).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }
}
