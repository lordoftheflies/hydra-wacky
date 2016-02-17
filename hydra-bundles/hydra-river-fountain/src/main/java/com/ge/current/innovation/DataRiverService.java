/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author lordoftheflies
 */
@Component
public class DataRiverService {

    @Value("${rabbitmq.queue.data}")
    private String queue;
    
    private static final Logger LOG = Logger.getLogger(DataRiverService.class.getName());

    
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private SimpleDateFormat sdf = new SimpleDateFormat();

    public void send(DataPoint... points) throws Exception {
        for (DataPoint point : points) {
            rabbitTemplate.convertAndSend(queue, point, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message msg) throws AmqpException {
                    LOG.info("Data-ingestion acknowledged: " + msg);
                    return msg;
                }
            });
        }
    }
}
