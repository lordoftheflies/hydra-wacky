/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.dataingestion.consumer.listeners;

import com.ge.current.innovation.DataPoint;
import com.ge.current.innovation.utils.JsonUtils;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author lordoftheflies
 */
@Component
public class MessageGatewayListener {

    private static final Logger LOG = Logger.getLogger(MessageGatewayListener.class.getName());

    @Value("${rabbitmq.queue.data}")
    private String dataQueue;

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    public void onMessage(String msg) throws IOException {
    public void onMessage(DataPoint msg) throws IOException {
//        DataPoint dp = new JsonUtils().read(DataPoint.class, msg);
        
        LOG.log(Level.INFO, "Ingest data-point to queue[name={0}]: {1}", new Object[]{dataQueue, msg});
        rabbitTemplate.convertAndSend(dataQueue, msg);
//        rabbitTemplate.convertAndSend(dataQueue, dp);
    }
    
//    public void onMessage(DataPoint dp) throws IOException {
////        DataPoint dp = new JsonUtils().read(DataPoint.class, msg);
//        LOG.log(Level.INFO, "Ingest data-point to queue[name={0}]: {1}", new Object[]{dataQueue, dp});
//        rabbitTemplate.convertAndSend(dataQueue, dp);
//    }
}
