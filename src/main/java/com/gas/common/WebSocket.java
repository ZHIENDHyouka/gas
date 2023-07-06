package com.gas.common;

import com.alibaba.fastjson2.JSON;
import com.gas.entity.ExcessGas;
import com.gas.entity.Humidity;
import com.gas.entity.ResultVO;
import com.gas.entity.Temperature;
import com.gas.mapper.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/webSocket")//主要是将目前的类定义成一个websocket服务器端, 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
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

    private TemperatureMapper getTemperatureMapper(){
        TemperatureMapper temperatureMapper = SpringContext.getBean(TemperatureMapper.class);
        return temperatureMapper;
    }

    private DeivceService getDeivceService(){
        return SpringContext.getBean(DeivceService.class);
    }

    private ExcessGasMapper getExcessGasMapper(){
        return SpringContext.getBean(ExcessGasMapper.class);
    }

    private HumidityMapper getHumidityMapper(){
        return SpringContext.getBean(HumidityMapper.class);
    }

    private HarmfulGasMapper getHarmfulGasMapper(){
        return SpringContext.getBean(HarmfulGasMapper.class);
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
        if ("1".equals(message)) {
            HashMap<String, Object> sendMap = new HashMap<>();
            //实时数据
            String deviceNumberData = this.updateDeviceNumber();
            String alarmInfoData = this.updateAlarmInfoNumber();
            String temperatureData = this.updateTemperatureData();
            String humidityData = this.updateHumidityData();
            String HarmfulGasData = this.updateHarmfulGas();
            sendMap.put("deviceNumberData",deviceNumberData);
            sendMap.put("alarmInfoData",alarmInfoData);
            sendMap.put("temperatureData",temperatureData);
            sendMap.put("humidityData",humidityData);
            sendMap.put("HarmfulGasData",HarmfulGasData);
            sendMessage(JSON.toJSONString(sendMap));
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
     * @param message 消息
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发
     * @param message 消息
     */
    public static void sendInfo(String message) throws IOException {
        log.info("信息:{}", message);
        for (WebSocket item : webSocketSet) {
            item.sendMessage(message);
        }
    }

    private String updateDeviceNumber(){
        String s= null;
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

    private String updateAlarmInfoNumber(){
        try {
            ExcessGasMapper excessGasMapper = getExcessGasMapper();
            Random random = new Random();
            //造假数据
            CreateDataUtils.createExcessGasData(random.nextInt(30));
            Integer number = excessGasMapper.queryRealTimeNumber(
                    DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT),
                    DateTimeUtil.getLocalDateTimeFormat(LocalDateTime.now().minusSeconds(2),
                            DateTimeUtil.DATETIMEFORMAT));
            HashMap<String, Object> alarmInfoMap = new HashMap<>();
            alarmInfoMap.put("number",number);
            alarmInfoMap.put("datetime", DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT).split(" ")[1]);
            return JSON.toJSONString(alarmInfoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String  updateTemperatureData(){
        String deviceId = "22SI-09DG-63KJ";//测试数据
        double value = new Random().nextDouble()*60-new Random().nextDouble()*10;
        String format = new DecimalFormat("#.00").format(value);
        int i = getTemperatureMapper().addTemperatureDateTest(new Temperature(0, Double.parseDouble(format),
                DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT), deviceId));
        Temperature temperature = getTemperatureMapper().queryRealTimeTemperatureByDeviceId(deviceId);
        return JSON.toJSONString(temperature.getTemperatureDate());
    }

    private String updateHumidityData(){
        String deviceId = "22SI-09DG-63KJ";//测试数据
        double value = new Random().nextDouble();
        String format = new DecimalFormat("#.00").format(value);
        int i = getHumidityMapper().addHumidityDateTest(new Humidity(0, Double.parseDouble(format),
                DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT), deviceId));
        Humidity humidity = getHumidityMapper().queryRealTimeHumidityByDeviceId(deviceId);
        return JSON.toJSONString(humidity.getHumidityDate());
    }

    private String updateHarmfulGas(){
        String id= "22SI-09DG-63KJ";
        CreateDataUtils.createHarmFulGas(50);
        List<Map<String, Object>> maps = getHarmfulGasMapper().queryRealTimeHarmfulGas(
                DateTimeUtil.getLocalDateTimeFormat(LocalDateTime.now().minusSeconds(2), DateTimeUtil.DATETIMEFORMAT),
                DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT),
                id);
        return JSON.toJSONString(maps);
    }
}