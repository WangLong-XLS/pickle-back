package com.pickle.utils.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // 1. 定义一个队列，参数为：队列名称、是否持久化
    @Bean
    public Queue myQueue() {
        return new Queue("myQueue", true);
    }

    // 2. 定义一个直连交换机
    @Bean
    public DirectExchange myExchange() {
        return new DirectExchange("myExchange");
    }

    // 3. 将队列通过路由键绑定到交换机
    @Bean
    public Binding binding(Queue myQueue, DirectExchange myExchange) {
        return BindingBuilder.bind(myQueue)
                .to(myExchange)
                .with("myRoutingKey");
    }
}