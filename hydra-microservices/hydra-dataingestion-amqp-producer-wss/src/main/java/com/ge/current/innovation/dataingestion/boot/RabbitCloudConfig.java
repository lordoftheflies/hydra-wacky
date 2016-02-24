package com.ge.current.innovation.dataingestion.boot;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * *
 *
 * @author lordoftheflies
 *
 */
@Configuration
@ComponentScan
@Profile("cloud")
public class RabbitCloudConfig extends AbstractCloudConfig {

    @Value("${rabbitmq.queue.data}")
    private String queue;

    @Bean
    public Queue simpleQueue() {
        return new Queue(queue);
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory().rabbitConnectionFactory());
        template.setRoutingKey(queue);
        return template;
    }
}
