package com.pickle.utils.redis;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 发送消息到我们之前配置的交换机和路由键
    public void send(String message) {
        rabbitTemplate.convertAndSend("myExchange", "myRoutingKey", message);
        System.out.println(" [x] Sent '" + message + "'");
    }
}