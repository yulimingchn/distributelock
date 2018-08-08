package com.dawn.banana.distributelock.common;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dawn on 2018/7/19.
 */
@Configuration
public class RabbitMQConfig {

    public static final String RABBITMQ_QUEUE_DAWN_USER_INFO = "queue_user_info";

    public static final String RABBITMQ_QUEUE_DAWN_USER_INFO_D = "queue_user_info_d";

    @Bean
    public Queue queue(){
        return new Queue(RABBITMQ_QUEUE_DAWN_USER_INFO);
    }


    @Bean
    public Queue queueD(){
        return new Queue(RABBITMQ_QUEUE_DAWN_USER_INFO_D);
    }



}
