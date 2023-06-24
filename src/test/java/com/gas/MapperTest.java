package com.gas;

import com.gas.entity.Temperature;
import com.gas.mapper.TemperatureMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {
    @Autowired
    private TemperatureMapper temperatureMapper;
    @Test
    public void test(){
//        22SI-09DG-63KJ
        List<Temperature> temperatures = temperatureMapper.queryConditionData("", "", "");
        System.out.println(temperatures);
    }
}
