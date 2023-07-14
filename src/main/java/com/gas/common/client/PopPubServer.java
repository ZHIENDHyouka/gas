package com.gas.common.client;

import com.alibaba.fastjson2.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.PubRequest;
import com.aliyuncs.iot.model.v20180120.PubResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.gas.utils.StringUtil;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class PopPubServer {
    private static String accessKey = "LTAI5tC8zq314HFEB4b39rSh";
    private static String accessKeySecret = "uQemdHk8E7tj1g77bYycJdwJcKKxP7";
    private static String iotInstanceId = "iot-06z00j7csd6edmj";
    private static String topicPath = "/j16hauhZMDv/1234/user/updateDevice";
    private static String regionId ="cn-shanghai";
    private static String apiUrl = "iot.cn-shanghai.aliyuncs.com";
    private static String productKey = "j16hauhZMDv";
    private final static Logger logger = LoggerFactory.getLogger(PopPubServer.class);

    public static boolean sendToTopic(String message)  {
        if (StringUtil.isEmpty(message)) {
            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKey, accessKeySecret); //cn-shenzhen为实例所属地域，需修改为您实例的所属地域。
            DefaultAcsClient client = new DefaultAcsClient(profile);
            PubRequest request = new PubRequest();
            request.setSysEndpoint(apiUrl); //所属地域的API服务端地址。
            request.setTopicFullName(topicPath); //发布消息的Topic。
            try {
                request.setMessageContent(Base64.encodeBase64String(message.getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            request.setProductKey(productKey);
            request.setIotInstanceId(iotInstanceId);
            request.setQos(1);
            try {
                PubResponse response = client.getAcsResponse(request);
                String s = new Gson().toJson(response);
                Map map = JSON.parseObject(s, Map.class);
                boolean flag = (boolean) map.get("success");
                return flag;
            } catch (ClientException e) {
                logger.info("ErrCode:" + e.getErrCode());
                logger.info("ErrMsg:" + e.getErrMsg());
                logger.info("RequestId:" + e.getRequestId());
            }
        }
        return false;
    }

}
