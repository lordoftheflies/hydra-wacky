/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.hydra.dataingestion;

import com.ge.current.innovation.hydra.storage.message.dal.AssetMeterRepository;
import com.ge.current.innovation.hydra.storage.message.dal.DataPointRepository;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

/**
 *
 * @author lordoftheflies
 */
@Configuration
@EnableRabbit
public class DataIngestionConfiguration implements RabbitListenerConfigurer {

    @Value("${rabbitmq.queue.data}")
    private String queue;

    @Value("${rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.port}")
    private String port;

    @Value("${rabbitmq.login}")
    private String login;

    @Value("${rabbitmq.password}")
    private String password;
    
    @Value("${river.dateFormat}")
    private String dateFormat;
    
    @Value("${rabbitmq.virtualHost}")
    private String vhost;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
//        connectionFactory.setUsername(login);
//        connectionFactory.setPassword(password);
//        connectionFactory.setVirtualHost(vhost);
        connectionFactory.setUri("amqp://a01ceff9-3b41-474d-8a6e-37194d7e406b:17vgaf0q7oag78ed6bstl9p3eu@10.72.6.45:5672/f7793d88-3051-4f31-b382-f427bcb10a4c");
        return connectionFactory;
    }

    @Bean
    public Queue simpleQueue() {
        return new Queue(queue);
    }

    @Autowired
    private DataPointRepository dataPointRepository;

    @Autowired
    private AssetMeterRepository assetMeterRepository;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(queue);
        return template;
    }

    @Bean
    public SimpleMessageListenerContainer listenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(simpleQueue());
        MessageListenerAdapter adapter = new MessageListenerAdapter(new DataIngestionListener(dataPointRepository, assetMeterRepository), "onMessage");
        listenerContainer.setMessageListener(adapter);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }

    @Bean
    public DefaultMessageHandlerMethodFactory handlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(new MappingJackson2MessageConverter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(handlerMethodFactory());
    }
}
