package com.ge.current.innovation.dataingestion.boot;

import com.ge.current.innovation.hydra.storage.dal.AssetMeterRepository;
import com.ge.current.innovation.hydra.storage.dal.DataPointRepository;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

/**
 * *
 *
 * @author lordoftheflies
 *
 */
@Configuration
@ComponentScan
@Profile("cloud")
public class RabbitCloudConfig extends AbstractCloudConfig implements RabbitListenerConfigurer {

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

    @Bean
    public SimpleMessageListenerContainer listenerContainer(DataPointRepository dataPointRepository, AssetMeterRepository assetMeterRepository) {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory().rabbitConnectionFactory());
        listenerContainer.setQueues(simpleQueue());
//        listenerContainer.setQueues();
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new DataIngestionListener(dataPointRepository, assetMeterRepository), "onMessage");
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
