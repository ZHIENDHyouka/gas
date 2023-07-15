package com.gas.common.client;

import com.alibaba.fastjson2.JSON;
import com.gas.common.SpringContext;
import com.gas.entity.*;
import com.gas.mapper.*;
import com.gas.utils.DateTimeUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.formula.functions.T;
import org.apache.qpid.jms.JmsConnection;
import org.apache.qpid.jms.JmsConnectionListener;
import org.apache.qpid.jms.message.JmsInboundMessageDispatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.net.URI;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//@Component
public class AmqpClient {
//    @Autowired
//    @Qualifier("deviceNameList")
//    private List<Device> deviceNameList;
//    @Autowired
//    private DeviceMapper deviceMapper;
//    @Autowired
//    private HarmfulGasMapper harmfulGasMapper;
//    @Autowired
//    private TemperatureMapper temperatureMapper;
//    @Autowired
//    private HumidityMapper humidityMapper;

    private final static Logger logger = LoggerFactory.getLogger(AmqpClient.class);
    private static String accessKey = "LTAI5tC8zq314HFEB4b39rSh";
    private static String accessSecret = "uQemdHk8E7tj1g77bYycJdwJcKKxP7";
    private static String consumerGroupId = "4sqWAPEVNfL7IrlmRFnS000100";
    private static String iotInstanceId = "iot-06z00j7csd6edmj" ;
    private static String clientId = "123";
    private static String host = "iot-06z00j7csd6edmj.amqp.iothub.aliyuncs.com";
    private static int connectionCount = 10;

    private static List<Device> deviceNameList = SpringContext.getBean("deviceNameList", List.class);
    private static boolean isExistConnection = false;

    //业务处理异步线程池，
    private final static ExecutorService executorService = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue(50000));

    public static void subscribe() {
        List<Connection> connections = new ArrayList<>();
        int realConnectionCount=connectionCount;
        for (int i = 0; i < connectionCount; i++) {
            long timeStamp = System.currentTimeMillis();
            //签名方法：支持hmacmd5、hmacsha1和hmacsha256。
            String signMethod = "hmacsha1";
            //userName组装方法
            String userName = clientId + "-" + i + "|authMode=aksign"
                    + ",signMethod=" + signMethod
                    + ",timestamp=" + timeStamp
                    + ",authId=" + accessKey
                    + ",iotInstanceId=" + iotInstanceId
                    + ",consumerGroupId=" + consumerGroupId
                    + "|";
            Connection connection=null;
            try {
                //计算签名，password组装方法
                String signContent = "authId=" + accessKey + "&timestamp=" + timeStamp;
                String password = doSign(signContent, accessSecret, signMethod);
                String connectionUrl = "failover:(amqps://" + host + ":5671?amqp.idleTimeout=80000)"
                        + "?failover.reconnectDelay=30";

                Hashtable<String, String> hashtable = new Hashtable<>();
                hashtable.put("connectionfactory.SBCF", connectionUrl);
                hashtable.put("queue.QUEUE", "default");
                hashtable.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.qpid.jms.jndi.JmsInitialContextFactory");
                Context context = new InitialContext(hashtable);
                ConnectionFactory cf = (ConnectionFactory)context.lookup("SBCF");
                Destination queue = (Destination)context.lookup("QUEUE");
                // 创建连接。
                connection = cf.createConnection(userName, password);
                connections.add(connection);

                ((JmsConnection)connection).addConnectionListener(myJmsConnectionListener);
                // 创建会话。
                // Session.AUTO_ACKNOWLEDGE: SDK自动ACK
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                connection.start();
                // 创建Receiver连接。
                MessageConsumer consumer = session.createConsumer(queue);
                consumer.setMessageListener(messageListener);
            } catch (Exception e) {
                logger.error("第"+(i+1)+"个连接创建出现错误!");
                realConnectionCount--;
                if (connection!=null){
                    try {
                        connection.close();
                    } catch (JMSException f) {
                        logger.error("连接关闭出现错误!");
                    }
                }
            }
        }
        logger.info(("目标连接数:"+connectionCount+",实际连接数:"+realConnectionCount));
        if (realConnectionCount>0) logger.info("连接成功!!");
        if (realConnectionCount==0){
            //关闭异步请求服务
            if (executorService!=null)
                executorService.shutdown();
        }else {
            isExistConnection = true;
        }
    }

    private static MessageListener messageListener = new MessageListener() {
        @Override
        public void onMessage(final Message message) {
            try {
                //建议异步处理收到的消息，确保onMessage函数里没有耗时逻辑。
                // 如果业务处理耗时过程过长阻塞住线程，可能会影响SDK收到消息后的正常回调。
//                executorService.submit(()->{
//                    processMessage(message);
//                });
                if (isExistConnection) {
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            processMessage(message);
                        }
                    });
                }
            } catch (Exception e) {
                logger.error("异步处理报错", e);
            }
        }
    };

    /**
     * 在这里处理收到消息后的具体业务逻辑。
     */
    private static void processMessage(Message message) {
        try {
            byte[] body = message.getBody(byte[].class);
            String content = new String(body);
            Map<String,Object> map = JSON.parseObject(content, Map.class);
            Lock lock = new ReentrantLock(true);//创建一个公平锁
            //获取设备名称
            String deviceName = map.get("deviceName").toString();
            try {
                //上锁
                lock.lock();
                Device device = deviceNameList.stream().filter(item -> deviceName.equals(item.getDeviceName())).findAny().orElse(null);
                ///如果是新设备自动添加
                if (device == null) {
                    logger.info("新设备接入！");
                    Device addDevice = new Device();
                    addDevice.setDeviceName(deviceName);
                    deviceNameList.add(addDevice);
                    int i = SpringContext.getBean(DeviceMapper.class).addDevice(new Device(0, deviceName, 0, ""));
                    if (!(i > 0)) logger.info("设备添加失败!");
                    else logger.info("设备添加成功！");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //释放锁
                lock.unlock();
            }

            //数据入库
            //后续如果插入数量大需要批量插入
//            CopyOnWriteArrayList<Temperature> inserTemperatures = new CopyOnWriteArrayList<Temperature>;
            Map<String,Object>dataMap = ((Map<String, Object>) map.get("items"));
            for (String key : dataMap.keySet()) {
                Map<String,Object> gasMap = (Map<String,Object>)dataMap.get(key);
                Double value = Double.valueOf(gasMap.get("value").toString()) ;
                long timeStamp = (long)gasMap.get("time");
                String datetimeFormat = DateTimeUtil.timeStampTransformString(timeStamp, DateTimeUtil.DATETIMEFORMAT);
                try {
                    if ("temperature".equals(key)){
                        int i = SpringContext.getBean(TemperatureMapper.class).addTemperatureData(new Temperature(0, value, datetimeFormat, deviceName));
                        if (i>0) logger.info("数据插入成功!温度");
                        else logger.info("数据插入失败!");
                    }else if ("humidity".equals(key)){
                        int i = SpringContext.getBean(HumidityMapper.class).addHumidityData(new Humidity(0, value, datetimeFormat, deviceName));
                        if (i>0) logger.info("数据插入成功!湿度");
                        else logger.info("数据插入失败!");
                    }else {
                        int i = SpringContext.getBean(HarmfulGasMapper.class).addHarmfulGasData(new HarmfulGas(0, key, value, datetimeFormat, deviceName));
                        if (i>0) logger.info("数据插入成功!"+key);
                        else logger.info("数据插入失败!");
                    }
                } catch (Exception e) {
                    logger.info("数据插入失败!");
                }
            }
        } catch (Exception e) {
            logger.error("处理业务数据报错 ", e);
        }
    }

    private static JmsConnectionListener myJmsConnectionListener = new JmsConnectionListener() {
        /**
         * 连接成功建立。
         */
        @Override
        public void onConnectionEstablished(URI remoteURI) {
            logger.info("onConnectionEstablished, remoteUri:{}", remoteURI);
        }

        /**
         * 尝试过最大重试次数之后，最终连接失败。
         */
        @Override
        public void onConnectionFailure(Throwable error) {
            logger.error("onConnectionFailure, {}", error.getMessage());
        }

        /**
         * 连接中断。
         */
        @Override
        public void onConnectionInterrupted(URI remoteURI) {
            logger.info("onConnectionInterrupted, remoteUri:{}", remoteURI);
        }

        /**
         * 连接中断后又自动重连上。
         */
        @Override
        public void onConnectionRestored(URI remoteURI) {
            logger.info("onConnectionRestored, remoteUri:{}", remoteURI);
        }

        @Override
        public void onInboundMessage(JmsInboundMessageDispatch envelope) {}

        @Override
        public void onSessionClosed(Session session, Throwable cause) {}

        @Override
        public void onConsumerClosed(MessageConsumer consumer, Throwable cause) {}

        @Override
        public void onProducerClosed(MessageProducer producer, Throwable cause) {}
    };

    /**
     * 计算签名，password组装方法
     */
    private static String doSign(String toSignString, String secret, String signMethod) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), signMethod);
        Mac mac = Mac.getInstance(signMethod);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(toSignString.getBytes());
        return Base64.encodeBase64String(rawHmac);
    }
}
