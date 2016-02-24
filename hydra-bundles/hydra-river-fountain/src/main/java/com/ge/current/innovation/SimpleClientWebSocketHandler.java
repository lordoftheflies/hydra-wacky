/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation;

import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 *
 * @author lordoftheflies
 */
public class SimpleClientWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOG = Logger.getLogger(SimpleClientWebSocketHandler.class.getName());

    @Autowired
    public SimpleClientWebSocketHandler() {
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String reply = "Fos: " + message.getPayload();
        session.sendMessage(new TextMessage(reply));
    }
}
