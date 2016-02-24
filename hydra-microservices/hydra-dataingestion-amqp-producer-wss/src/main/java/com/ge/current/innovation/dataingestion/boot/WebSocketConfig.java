/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.dataingestion.boot;

import com.ge.current.innovation.dataingestion.handlers.AlertMessageHandler;
import com.ge.current.innovation.dataingestion.handlers.DataIngestionMessageHandler;
import com.ge.current.innovation.dataingestion.service.RabbitMqProducerService;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 *
 * @author lordoftheflies
 */
@Configuration
//@Configuration
@EnableWebMvc
@EnableWebSocket
//@EnableWebSocketMessageBroker
//public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/topic");
////        config.setApplicationDestinationPrefixes("/app");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/data").withSockJS();
//    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(alertWebSocketHandler(), "/alert");//, "/echo-issue4");
        registry.addHandler(dataIngestionWebSocketHandler(), "/data");//, "/echo-issue4");
//        registry.addHandler(snakeWebSocketHandler(), "/snake");
//
//        registry.addHandler(alertWebSocketHandler(), "/sockjs/echo").withSockJS();
//        registry.addHandler(alertWebSocketHandler(), "/sockjs/echo-issue4").withSockJS().setHttpMessageCacheSize(20000);
//
//        registry.addHandler(snakeWebSocketHandler(), "/sockjs/snake").withSockJS();
    }
            

    @Bean
    public WebSocketHandler alertWebSocketHandler() {
        return new AlertMessageHandler();
    }
    
    @Bean
    public WebSocketHandler dataIngestionWebSocketHandler() {
        return new DataIngestionMessageHandler();
    }


    // Allow serving HTML files through the default Servlet
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    
    
    
//    @Bean
//    public JettyWebSocketClient client() {
//        return new JettyWebSocketClient();
//    }
}
