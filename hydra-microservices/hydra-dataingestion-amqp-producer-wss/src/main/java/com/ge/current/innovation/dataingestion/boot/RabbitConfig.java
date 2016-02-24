/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.dataingestion.boot;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author lordoftheflies
 */
@Configuration
@EnableRabbit
public class RabbitConfig {

    @Value("${rabbitmq.queue.data}")
    private String dataQuew;

    @Value("${rabbitmq.queue.alert}")
    private String alertQueue;

    @Value("${rabbitmq.host}")
    private String host;
//    @Value("${rabbitmq.virtualHost}")
//    private String virtualHost;

    @Value("${rabbitmq.port}")
    private String port;

    @Value("${rabbitmq.login}")
    private String login;

    @Value("${rabbitmq.password}")
    private String password;

//    @Value("${river.dateFormat}")
//    private String dateFormat;

//    @Bean
//    public ConnectionFactory connectionFactory() {
//        ConnectionFactory connectionFactory = new CachingConnectionFactory(host, Integer.parseInt(port));
//        connectionFactory.setUsername(login);
//        connectionFactory.setPassword(password);
//        connectionFactory.setHost(host);
////        connectionFactory.setVirtualHost(virtualHost);
//        return connectionFactory;
//    }
////
////
//
//    @Bean
//    public RabbitTemplate rabbitTemplate() {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory());
//        template.setRoutingKey(alertQueue);
//        template.setRoutingKey(dataQuew);
//        return template;
//    }
}
