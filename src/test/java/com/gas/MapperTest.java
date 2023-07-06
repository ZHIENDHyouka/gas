package com.gas;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSONArray;
import com.gas.entity.ExcessGas;
import com.gas.entity.Humidity;
import com.gas.entity.Temperature;
import com.gas.mapper.TemperatureMapper;
import com.gas.mapper.TestMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

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

}
