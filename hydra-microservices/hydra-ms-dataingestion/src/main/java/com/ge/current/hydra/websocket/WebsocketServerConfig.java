package com.ge.current.hydra.websocket;


import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
public class WebsocketServerConfig {

    /**
     * @return DataIngestionWebsocket
     */
    @Bean
    public DataStreamServerEndPoint dataIngestionWebsocketEndpoint() {
        return new DataStreamServerEndPoint();
    }

    /**
     * @return -
     */
    @Bean
    public ServerEndpointExporter endpointExporter() {
        return new ServerEndpointExporter();
    }
//    @Bean
//    public ServletContextAware endpointExporterInitializer(final ApplicationContext applicationContext) {
//        return new ServletContextAware() {
//
//            @Override
//            public void setServletContext(ServletContext servletContext) {
//                ServerEndpointExporter serverEndpointExporter = new ServerEndpointExporter();
//                serverEndpointExporter.setApplicationContext(applicationContext);
//                serverEndpointExporter.afterPropertiesSet();
//            }
//        };
//    }

//    @Bean
//    public RabbitTemplate rabbitTemplate() {
//        return new RabbitTemplate(rabbitConnectionFactory());
//    }
//
    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(rabbitConnectionFactory());
    }
//
    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }
//    /**
//     * @return -
//     */
//    @Bean
//    public WebSocketContainer getWebsocketContainer() {
//        return ContainerProvider.getWebSocketContainer();
//    }
}
