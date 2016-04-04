/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.hydra.websocket;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author lordoftheflies
 */
@Component
public class AssetHandler {

    private static final Logger LOG = Logger.getLogger(AssetHandler.class.getName());

    @Autowired
    private RabbitAdmin rabbitAdmin;

    public boolean exist(String nodeId) {
        boolean queueExists = rabbitAdmin.getRabbitTemplate().expectedQueueNames() != null
                && rabbitAdmin.getRabbitTemplate().expectedQueueNames().contains(nodeId);
        // Declare queue for the data-stream.
        if (!queueExists) {
            LOG.log(Level.INFO, "Queue for node(id={0}) not exist in MQ.", nodeId);
        }
        return queueExists;
    }

    public void attachNode(String nodeId) {
        LOG.log(Level.INFO, "Create asset for node(id={0}) in Predix-Asset ...", nodeId);
        
        LOG.log(Level.INFO, "Create queue for node(id={0}) in MQ ...", nodeId);
        rabbitAdmin.declareQueue(new Queue(nodeId));
        rabbitAdmin.initialize();
        LOG.log(Level.INFO, "Node(id={0}) attached to system", nodeId);
        
    }

    public void detachNode(String nodeId) {
        LOG.log(Level.INFO, "Delete queue for node(id={0}) in MQ.", nodeId);
        rabbitAdmin.deleteQueue(nodeId);
        rabbitAdmin.initialize();
    }
}
