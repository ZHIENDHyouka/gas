package com.gas;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSONArray;
import com.gas.entity.ExcessGas;
import com.gas.entity.Humidity;
import com.gas.entity.Temperature;
import com.gas.mapper.TemperatureMapper;
import com.gas.mapper.TestMapper;
import com.gas.utils.DateTimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.gas.utils.DateTimeUtil.DATEFORMAT;
import static com.gas.utils.DateTimeUtil.getLocalDateTimeFormat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)

public class MapperTest {
    @Autowired
    private TemperatureMapper temperatureMapper;
    @Autowired
    private TestMapper testMapper;
    @Test
    public void test(){
//        22SI-09DG-63KJ
        List<Temperature> temperatures = temperatureMapper.queryConditionData("", "", "");
        System.out.println(temperatures);
    }
    @Test
    public void test1(){
        List<Humidity> list =new ArrayList<>();
        Humidity h1 = new Humidity(1, 3.14, "2013-12-12", "abcd");
        Humidity h2 = new Humidity(2, 3.14, "2013-12-12", "abcd");
        Humidity h3 = new Humidity(3, 3.14, "2013-12-12", "abcd");
        Humidity h4 = new Humidity(4, 3.14, "2013-12-12", "abcd");
        Humidity h5 = new Humidity(5, 3.14, "2013-12-12", "abcd");
        list.add(h1);
        list.add(h2);
        list.add(h3);
        list.add(h4);
        list.add(h5);
        EasyExcel.write("d:/Humidity.xlsx", Humidity.class).sheet("test1").doWrite(list);
    }

    @Test
    public void test3(){
        String addFormatDay = DateTimeUtil.getAddFormatDay(1, "2022-01-01 00:00:00", DateTimeUtil.DATETIMEFORMAT, DateTimeUtil.DATETIMEFORMAT);
        System.out.println(addFormatDay);
        long stringTimeStamp1 = DateTimeUtil.getStringTimeStamp("2022-01-01 00:00:00", DateTimeUtil.DATETIMEFORMAT);
        long stringTimeStamp2 = DateTimeUtil.getStringTimeStamp("2022-01-02 00:00:00", DateTimeUtil.DATETIMEFORMAT);
        long interval = (stringTimeStamp2-stringTimeStamp1)/1000/60/60/24;
        System.out.println(interval);
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(stringTimeStamp2 / 1000, 0, ZoneOffset.ofHours(8));
        String localDateTimeFormat = getLocalDateTimeFormat(localDateTime, DATEFORMAT);
        System.out.println(localDateTimeFormat);
    }

}
