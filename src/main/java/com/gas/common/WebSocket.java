package com.gas.common;

import com.alibaba.fastjson2.JSON;
import com.gas.entity.ExcessGas;
import com.gas.entity.Humidity;
import com.gas.entity.ResultVO;
import com.gas.entity.Temperature;
import com.gas.mapper.*;
import com.gas.service.AppService;
import com.gas.service.DeivceService;
import com.gas.utils.CreateDataUtils;
import com.gas.utils.DateTimeUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/webSocket")
//主要是将目前的类定义成一个websocket服务器端, 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
@Component
@EnableScheduling// cron定时任务
@Data
@Slf4j
public class WebSocket {
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    public static CopyOnWriteArraySet<WebSocket> getWebSocketSet() {
        return webSocketSet;
    }

    public static void setWebSocketSet(CopyOnWriteArraySet<WebSocket> webSocketSet) {
        WebSocket.webSocketSet = webSocketSet;
    }

    //获取对应的mapper实例对象
    private RecordDeviceNumberMapper getRecordDeviceNumberMapper() {
        RecordDeviceNumberMapper recordDeviceNumberMapper = SpringContext.getBean(RecordDeviceNumberMapper.class);
        return recordDeviceNumberMapper;
    }

    private DeviceMapper getDeviceMapper() {
        DeviceMapper deviceMapper = SpringContext.getBean(DeviceMapper.class);
        return deviceMapper;
    }

    private TemperatureMapper getTemperatureMapper() {
        TemperatureMapper temperatureMapper = SpringContext.getBean(TemperatureMapper.class);
        return temperatureMapper;
    }

    private DeivceService getDeivceService() {
        return SpringContext.getBean(DeivceService.class);
    }

    private ExcessGasMapper getExcessGasMapper() {
        return SpringContext.getBean(ExcessGasMapper.class);
    }

    private HumidityMapper getHumidityMapper() {
        return SpringContext.getBean(HumidityMapper.class);
    }

    private HarmfulGasMapper getHarmfulGasMapper() {
        return SpringContext.getBean(HarmfulGasMapper.class);
    }

    private AppService getAppService() {
        return SpringContext.getBean(AppService.class);
    }

    /**
     * 连接建立成功调用的方法
     *
     * @param session 会话
     */
    @OnOpen
    public void onOpen(Session session) throws Exception {
        log.info("连接成功!");
        this.session = session;
        webSocketSet.add(this);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
//        log.info("收到信息"+message);
        Map map = JSON.parseObject(message, Map.class);
        String code = map.get("code").toString();
        if ("1".equals(code)) {
            HashMap<String, Object> sendMap = new HashMap<>();
            //实时图表数据
            String deviceNumberData = this.updateDeviceNumber();
            String alarmInfoData = this.updateAlarmInfoNumber();
            String temperatureData = this.updateTemperatureData();
            String humidityData = this.updateHumidityData();
            String HarmfulGasData = this.updateHarmfulGas();
            sendMap.put("code", 1);
            sendMap.put("deviceNumberData", deviceNumberData);
            sendMap.put("alarmInfoData", alarmInfoData);
            sendMap.put("temperatureData", temperatureData);
            sendMap.put("humidityData", humidityData);
            sendMap.put("HarmfulGasData", HarmfulGasData);
            sendMessage(JSON.toJSONString(sendMap));
        } else if ("2".equals(code)) {
            //报警信息数量
            HashMap<String, Object> result = new HashMap<>();
            String alarmNumber = this.updateDayAlarmInfoNumber();
            if (!"0".equals(alarmNumber)) {
                result.put("code", 2);
                result.put("alarmNumber", alarmNumber);
                sendMessage(JSON.toJSONString(result));
            }
        } else if ("3".equals(code)) {
            //报警数据
            Integer number = (Integer) map.get("data");
            String now = DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT);
            String date = now.split(" ")[0];
            date += " 00:00:00";
            List<ExcessGas> excessGases = getExcessGasMapper().queryDayAllAlarmData(date);
            HashMap<String, Object> result = new HashMap<>();
            if (!(number == excessGases.size())) {
                result.put("code", 3);
                result.put("data", excessGases);
                sendMessage(JSON.toJSONString(result));
            }
        } else if ("4".equals(code)) {
            String s = this.updateAppRealTimeData(map);
            sendMessage(s);
        }
//        this.sendMessage(message);
//        for (WebSocket item : webSocketSet) {
//            try {
//                if (this==item) {
//
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session 会话
     * @param error   错误信息
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("连接异常！");
        error.printStackTrace();
    }

    /**
     * 发送信息
     * 单发
     *
     * @param message 消息
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发
     *
     * @param message 消息
     */
    public static void sendInfo(String message) throws IOException {
        log.info("信息:{}", message);
        for (WebSocket item : webSocketSet) {
            item.sendMessage(message);
        }
    }

    private String updateDeviceNumber() {
        String s = null;
        try {
            DeviceMapper deviceMapper = getDeviceMapper();
            RecordDeviceNumberMapper recordDeviceNumberMapper = getRecordDeviceNumberMapper();
            int number = deviceMapper.queryDeviceRunNumber();
            recordDeviceNumberMapper.insertRecordNumberInfo(number, DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT));
            ResultVO deviceRunNumber = getDeivceService().getDeviceRunNumber();
            s = JSON.toJSONString(deviceRunNumber);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    private String updateAlarmInfoNumber() {
        try {
            ExcessGasMapper excessGasMapper = getExcessGasMapper();
            //造假数据
//            CreateDataUtils.createExcessGasData(random.nextInt(30));
            Integer number = excessGasMapper.queryRealTimeNumber(
                    DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT),
                    DateTimeUtil.getLocalDateTimeFormat(LocalDateTime.now().minusSeconds(2),
                            DateTimeUtil.DATETIMEFORMAT));
            HashMap<String, Object> alarmInfoMap = new HashMap<>();
            alarmInfoMap.put("number", number);
            alarmInfoMap.put("datetime", DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT).split(" ")[1]);
            return JSON.toJSONString(alarmInfoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String updateTemperatureData() {
        String deviceId = "1234";//测试数据
//        double value = new Random().nextDouble() * 60 - new Random().nextDouble() * 10;
//        String format = new DecimalFormat("#.00").format(value);
//        int i = getTemperatureMapper().addTemperatureDateTest(new Temperature(0, Double.parseDouble(format),
//                DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT), deviceId));
        Temperature temperature = getTemperatureMapper().queryRealTimeTemperatureByDeviceId(deviceId);
        return JSON.toJSONString(temperature.getTemperatureDate());
    }

    private String updateHumidityData() {
        String deviceId = "1234";//测试数据
//        double value = new Random().nextDouble();
//        String format = new DecimalFormat("#.00").format(value);
//        int i = getHumidityMapper().addHumidityDateTest(new Humidity(0, Double.parseDouble(format),
//                DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT), deviceId));
        Humidity humidity = getHumidityMapper().queryRealTimeHumidityByDeviceId(deviceId);
        return JSON.toJSONString(humidity.getHumidityDate());
    }

    private String updateHarmfulGas() {
        String id = "1234";
//        CreateDataUtils.createHarmFulGas(50);
        String now = DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT);
        long stringTimeStamp = DateTimeUtil.getStringTimeStamp(now, DateTimeUtil.DATETIMEFORMAT);
        //延迟5s
        stringTimeStamp-=1000*3;
        now = DateTimeUtil.timeStampTransformString(stringTimeStamp, DateTimeUtil.DATETIMEFORMAT);
        System.out.println(now);
        List<Map<String, Object>> maps = getHarmfulGasMapper().queryRealTimeHarmfulGas(
                DateTimeUtil.getLocalDateTimeFormat(LocalDateTime.now().minusSeconds(5), DateTimeUtil.DATETIMEFORMAT),
                now,
                id);
        HashMap<String, Object> result = new HashMap<>();
        result.put("data", maps);
        result.put("datetime", now.split(" ")[1]);
        return JSON.toJSONString(result);
    }

    private String updateDayAlarmInfoNumber() {
        String now = DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT);
        String date = now.split(" ")[0];
        date += " 00:00:00";
        int number = getExcessGasMapper().queryDayAllAlarmNumber(date);
        return JSON.toJSONString(number);
    }

    private String updateAppRealTimeData(Map map) {
        HashMap<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = (List<Map<String, Object>>) getAppService().getGasNameAndNewData().getData();
        HashMap<String, Object> o = new HashMap<>();
        for (Map m : data) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String data1 = decimalFormat.format(Double.parseDouble(m.get("data").toString()));
            // double v = Double.parseDouble(m.get("data").toString()) + random.nextInt(10);
            m.put("data", data1);
            if (map.get("data").toString().equals(m.get("name"))) {
                Queue importDataQueue = SpringContext.getBean("importDataQueue", Queue.class);
                String date = "";
                if (importDataQueue.size()>0){
                    date=importDataQueue.poll().toString();
                }else {
                    date=DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT);
                }
                o.put("date", date);
                o.put("data", data1);
            }
        }
        result.put("realTimeData", data);
        result.put("realTimeStatistic", o);
        return JSON.toJSONString(result);
    }
}