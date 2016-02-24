/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.dataingestion.service;

import java.util.logging.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 *
 * @author lordoftheflies
 */
public class AlertMessageHandler extends TextWebSocketHandler {

    private static final Logger LOG = Logger.getLogger(AlertMessageHandler.class.getName());

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String reply = "Process alert: " + message.getPayload();
        LOG.info(reply);
        session.sendMessage(new TextMessage("OK"));
    }
}
