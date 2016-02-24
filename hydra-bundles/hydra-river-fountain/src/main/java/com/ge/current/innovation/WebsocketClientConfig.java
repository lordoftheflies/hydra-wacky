/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.jetty.JettyWebSocketClient;

@Configuration
public class WebsocketClientConfig {

    @Bean
    public WebSocketConnectionManager connectionManager() {
        WebSocketConnectionManager manager = new WebSocketConnectionManager(client(), handler(), RiverFountainApplication.ALERT_WS_URI);
        manager.setAutoStartup(true);
        return manager;
    }

    @Bean
    public JettyWebSocketClient client() {
        return new JettyWebSocketClient();
    }

    @Bean
    public SimpleClientWebSocketHandler handler() {
        return new SimpleClientWebSocketHandler();
    }
    
}
