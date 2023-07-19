package com.gas;

import com.gas.entity.Device;
import com.gas.entity.HarmfulGas;
import com.gas.entity.Humidity;
import com.gas.entity.Temperature;
import com.gas.mapper.DeviceMapper;
import com.gas.mapper.HarmfulGasMapper;
import com.gas.mapper.HumidityMapper;
import com.gas.mapper.TemperatureMapper;
import com.gas.utils.DateTimeUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateGasData {
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    private  String deviceIdArr[]={"23BG-67TY-91KL","09TH-56RE-23FG","76FR-12GT-88HY","45UY-87JK-09DF","33RT-65FH-11DX",
            "54GB-28HN-94KL","87TH-06UY-77PL","22SI-09DG-63KJ","99RS-44UI-12RE","71HI-65OP-91KL"};
    private String gasNameArr[] = {"PM2.5","PM10","SO2","NO2","CO","O3"};

    @Test
    public void createDeviceData(){
        List<Device> deviceDataList = createDeviceDataList(deviceIdArr.length);
        SqlSession sqlSession = this.sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        DeviceMapper mapper = sqlSession.getMapper(DeviceMapper.class);
        deviceDataList.stream().forEach(device -> mapper.addDeviceDataTest(device));
        sqlSession.commit();
        sqlSession.clearCache();
    }

    @Test
    public void createTempertureDate(){
        //批处理生成数据
        List<Temperature> dateList = createTemperatureDataList(30000);
        //使用批处理
        SqlSession sqlSession = this.sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        TemperatureMapper mapper = sqlSession.getMapper(TemperatureMapper.class);
        dateList.stream().forEach(temperature -> mapper.addTemperatureDateTest(temperature));
        sqlSession.commit();
        sqlSession.clearCache();
    }

    @Test
    public void createHumidityDate(){
        //批处理生成数据
        List<Humidity> dateList = createHumidityDataList(50000);
        //使用批处理
        SqlSession sqlSession = this.sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        HumidityMapper mapper = sqlSession.getMapper(HumidityMapper.class);
        dateList.stream().forEach(humidity -> mapper.addHumidityDateTest(humidity));
        sqlSession.commit();
        sqlSession.clearCache();
    }
    
    @Test
    public void createHarmfulGasData(){
        List<HarmfulGas> harmfulGasDataList = createHarmfulGasDataList(50000);
        //使用批处理
        SqlSession sqlSession = this.sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        HarmfulGasMapper mapper = sqlSession.getMapper(HarmfulGasMapper.class);
        harmfulGasDataList.stream().forEach(harmfulGas -> mapper.addHarmfulGasDateTest(harmfulGas));
        sqlSession.commit();
        sqlSession.clearCache();
    }

    private List<Device> createDeviceDataList(int n){
        ArrayList<Device> devices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Device device = new Device();
            device.setDeviceName(deviceIdArr[i]);
            devices.add(device);
        }
        return devices;
    }

    private List<HarmfulGas> createHarmfulGasDataList(int n){
        ArrayList<HarmfulGas> harmfulGases = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            double data = random.nextDouble()*10;
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            data=Double.parseDouble(decimalFormat.format(data));
            HarmfulGas harmfulGas = new HarmfulGas(0, gasNameArr[random.nextInt(6)], data, getIndate(n), deviceIdArr[random.nextInt(10)]);
            harmfulGases.add(harmfulGas);
        }
        return harmfulGases;
    }

    private List<Humidity> createHumidityDataList(int n){
        ArrayList<Humidity> humidityList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            double data = random.nextDouble();
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            data=Double.parseDouble(decimalFormat.format(data));
            Humidity humidity = new Humidity(0,data,getIndate(n),deviceIdArr[random.nextInt(10)]);
            humidityList.add(humidity);
        }
        return humidityList;
    }

    private List<Temperature> createTemperatureDataList(int n){
        ArrayList<Temperature> temperatureList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            double data = random.nextDouble()+(random.nextInt(55)-15);
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            data=Double.parseDouble(decimalFormat.format(data));
            Temperature temperature = new Temperature(0,data,getIndate(n),deviceIdArr[random.nextInt(10)]);
            temperatureList.add(temperature);
        }
        return temperatureList;
    }

    @Test
    public void test1(){
        for (int i = 0; i < 1000; i++) {
            System.out.println(getDateTime());
        }
    }

    private String getDateTime(){
        //获取当前时间
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();

        //设置指定时间
        long before = 0;
        String format= "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse("2020-1-1 00:00:00");
            before = date.getTime();
            Random random = new Random();
            //产生long类型指定范围随机数
            long randomDate = before + (long) (random.nextFloat() * (now - before + 1));
            Date date1 = new Date(randomDate);
            format = sdf.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format;
    }

    private String getIndate(int minute){
        String now = DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT);
        String s = DateTimeUtil.addTimeStamp(now, 1000L * 60 * minute);
        return s;
    }
}
