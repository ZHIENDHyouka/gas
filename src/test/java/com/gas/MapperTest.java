package com.gas;

import com.gas.entity.Temperature;
import com.gas.mapper.TemperatureMapper;
import com.gas.mapper.TestMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        int i = testMapper.updateById(11);
        System.out.println(i);
    }
}
