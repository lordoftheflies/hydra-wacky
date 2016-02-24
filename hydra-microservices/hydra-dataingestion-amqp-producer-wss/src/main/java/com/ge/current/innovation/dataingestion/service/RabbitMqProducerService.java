/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.dataingestion.service;

import com.ge.current.innovation.DataPoint;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author lordoftheflies
 */
@Service
public class RabbitMqProducerService {

    private static final Logger LOG = Logger.getLogger(RabbitMqProducerService.class.getName());

    @Value("${rabbitmq.queue.data}")
    private String dataQuew;

    @Value("${rabbitmq.queue.alert}")
    private String alertQueue;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Collection<DataPoint> points) {
        LOG.log(Level.INFO, "Produce data-points ({0}) to aggregation-tier ...", points.size());
        for (DataPoint point : points) {
            rabbitTemplate.convertAndSend(dataQuew, point);
        }
        LOG.info("Producer sent the data-points.");
    }

    public void send(DataPoint... points) {
        LOG.log(Level.INFO, "Produce data-points ({0}) to aggregation-tier ...", points.length);
        for (int i = 0; i < points.length; i++) {
            DataPoint point = points[i];
            rabbitTemplate.convertAndSend(dataQuew, point);
        }
        LOG.info("Producer sent the data-points.");
    }

    public void notify(Exception ex) {
        LOG.log(Level.WARNING, "Produce notification from the exception", ex);
        rabbitTemplate.convertAndSend(alertQueue, ex.getMessage());
    }

    public void notify(String message) {
        LOG.log(Level.WARNING, "Produce notification: {0}", message);
        rabbitTemplate.convertAndSend(alertQueue, message);
    }
}
