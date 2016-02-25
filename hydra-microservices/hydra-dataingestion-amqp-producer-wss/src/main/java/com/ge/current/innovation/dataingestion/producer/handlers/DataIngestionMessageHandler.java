/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.dataingestion.producer.handlers;

import com.ge.current.innovation.DataPoint;
import com.ge.current.innovation.dataingestion.producer.service.RabbitMqProducerService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 *
 * @author lordoftheflies
 */
public class DataIngestionMessageHandler extends TextWebSocketHandler {

    private static final Logger LOG = Logger.getLogger(DataIngestionMessageHandler.class.getName());

    @Autowired
    private RabbitMqProducerService rabbitMqProducerService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOG.log(Level.INFO, "Process data: {0}", message.getPayload());
        rabbitMqProducerService.send(new DataPoint().parse(message.getPayload()));
        session.sendMessage(new TextMessage("INGESTED"));
    }
}
