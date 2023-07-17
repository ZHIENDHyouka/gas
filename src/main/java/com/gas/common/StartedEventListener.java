package com.gas.common;

import com.gas.common.client.AmqpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 监听springboot启动
 * 启动之后开启一些业务
 */
@Component
public class StartedEventListener implements ApplicationListener<ApplicationStartedEvent> {
    private final static Logger logger = LoggerFactory.getLogger(StartedEventListener.class);
    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        logger.info("开始启动Amqp客户端");
//        AmqpClient.subscribe();
    }
}
