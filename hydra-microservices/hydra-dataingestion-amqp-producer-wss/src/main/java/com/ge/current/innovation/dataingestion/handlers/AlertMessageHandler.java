/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.dataingestion.handlers;

import com.ge.current.innovation.DataPoint;
import com.ge.current.innovation.dataingestion.service.RabbitMqProducerService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 *
 * @author lordoftheflies
 */
@Component
public class AlertMessageHandler extends TextWebSocketHandler {

    private static final Logger LOG = Logger.getLogger(AlertMessageHandler.class.getName());

    @Autowired
    private RabbitMqProducerService rabbitMqProducerService;


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOG.log(Level.INFO, "Process alert: {0}", message.getPayload());
        rabbitMqProducerService.notify(message.getPayload());
        session.sendMessage(new TextMessage("NOTIFIED"));
    }
}
