package com.pickle.sys.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

    @RabbitListener(queues = "myQueue")
    public void receiveMessage(String message) {
        System.out.println(" [x] Received '" + message + "'");
        // 在这里编写你的业务逻辑，比如处理订单、记录日志等
    }
}