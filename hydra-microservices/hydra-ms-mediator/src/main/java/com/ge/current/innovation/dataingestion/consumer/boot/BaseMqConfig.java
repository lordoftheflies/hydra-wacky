/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.dataingestion.consumer.boot;

import com.ge.current.innovation.DataPoint;
import com.ge.current.innovation.dataingestion.consumer.listeners.DataIngestionReceiver;
import com.ge.current.innovation.dataingestion.consumer.listeners.MessageGatewayListener;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

/**
 *
 * @author lordoftheflies
 */
public abstract class BaseMqConfig implements RabbitListenerConfigurer{
    
    public static final String EXCHANGE_INGESTION = "ingestion-exchange";

    @Value("${rabbitmq.queue.data}")
    protected String dataQueue;

    @Value("${rabbitmq.queue.data}")
    protected String alerQueue;

    @Value("${rabbitmq.host}")
    protected String host;

    @Value("${rabbitmq.port}")
    protected String port;

    @Value("${rabbitmq.login}")
    protected String login;

    @Value("${rabbitmq.password}")
    protected String password;

    @Value("${river.dateFormat}")
    protected String dateFormat;

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(handlerMethodFactory());
    }

    @Bean
    public Queue simpleQueue() {
        return new Queue(dataQueue);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_INGESTION);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(EXCHANGE_INGESTION);
    }
    
    @Bean
    public DataIngestionReceiver dataStreamConsumer() {
        return new DataIngestionReceiver();
    }
    
    @Bean
    public MessageGatewayListener messageStreamGatewayConsumer() {
        return new MessageGatewayListener();
    }
    
    @Bean(name = "gw")
    public SimpleMessageListenerContainer messageGatewayListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer(connectionFactory());
//        listenerContainer.setQueues(simpleQueue());
//        MessageListenerAdapter adapter = new MessageListenerAdapter(dataStreamConsumer(), "handleMessage");
        MessageListenerAdapter adapter = new MessageListenerAdapter(messageStreamGatewayConsumer(), "onMessage");
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new DataIngestionReceiver(), "handleMessage");
        listenerContainer.setMessageListener(adapter);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        listenerContainer.setMessageConverter(jsonMessageConverter());
        return listenerContainer;
    }
//    @Bean(name = "gw")
    @Bean(name = "data")
    public SimpleMessageListenerContainer dataIngestionListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer(connectionFactory());
        listenerContainer.setQueues(simpleQueue());
        MessageListenerAdapter adapter = new MessageListenerAdapter(dataStreamConsumer(), "onMessage");
        listenerContainer.setMessageListener(adapter);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        listenerContainer.setMessageConverter(jsonMessageConverter());
        return listenerContainer;
    }

    @Bean
    public DefaultClassMapper typeMapper() {
        DefaultClassMapper typeMapper = new DefaultClassMapper();
        typeMapper.setDefaultType(DataPoint.class);
        return typeMapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(typeMapper());
        return converter;
    }

    @Bean
    public org.springframework.messaging.converter.MessageConverter jackson2Converter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        return converter;
    }

    @Bean
    public DefaultMessageHandlerMethodFactory handlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(jackson2Converter());
        return factory;
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(dataQueue);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }
    
    public abstract ConnectionFactory connectionFactory();
}
