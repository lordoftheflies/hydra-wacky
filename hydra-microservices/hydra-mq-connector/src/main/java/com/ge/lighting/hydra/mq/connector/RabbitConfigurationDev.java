/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.mq.connector;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ge.lighting.hydra.datastore.DataStore;
import com.ge.lighting.hydra.mq.connector.rabbitmq.DataCollectionService;

/**
 *
 * @author predix
 */
@Configuration
@EnableRabbit
@EnableAutoConfiguration
@ComponentScan
@Profile("dev")
public class RabbitConfigurationDev implements RabbitListenerConfigurer {

	static final String DEVELOPMENT_ADDRESS = "localhost";
	
    @Autowired
    private DefaultMessageHandlerMethodFactory handlerMethodFactory;

    @Autowired
    private DataCollectionService receiver;

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rler) {
        rler.setMessageHandlerMethodFactory(handlerMethodFactory);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
//        factory.setMessageConverter(messageConverter());
//        factory.setConcurrentConsumers();
        factory.setMaxConcurrentConsumers(10);
        return factory;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(DEVELOPMENT_ADDRESS);
//        connectionFactory.setHost("3.30.247.197");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("hydra");
        connectionFactory.setPassword("ombre2383");
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory());
        admin.setIgnoreDeclarationExceptions(true);
        return admin;
    }

//    @Bean
//    public Queue devQueue() {
//        return new Queue("test-rabbitmq");
//    }
    @Bean
    public Queue dataQueue() {
        return new Queue("data");
    }

//    @Bean
//    public Queue disconnectQueue() {
//        return new Queue("disconnect");
//    }
//    
//    public Queue connectQueue() {
//        return new Queue("connect");
//    }
    private static ObjectMapper mapper = new ObjectMapper();

    @Bean
    public DefaultMessageHandlerMethodFactory handlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(jackson2Converter());
        return factory;
    }

//    @Bean
//    public MessageConverter messageConverter() {
//        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
//        converter.setJsonObjectMapper(mapper);
//        return converter;
//    }
    @Bean
    public MappingJackson2MessageConverter jackson2Converter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        return converter;
    }

    @Bean
    public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
        UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();

//        factory.addBuilderCustomizers((UndertowBuilderCustomizer) (io.undertow.Undertow.Builder builder) -> {
//            builder.addHttpListener(8080, "0.0.0.0");
//        });

        return factory;
    }
    
    @Bean
    public DataStore dataStore() {
    	return new DataStore();
    }
}
