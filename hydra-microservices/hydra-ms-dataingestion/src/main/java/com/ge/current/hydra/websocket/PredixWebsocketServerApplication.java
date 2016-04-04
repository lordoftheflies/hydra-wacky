package com.ge.current.hydra.websocket;

import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.web.context.support.StandardServletEnvironment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.ge.current.hydra",
    "com.ge.current.hydra.websocket"
})
@EnableAutoConfiguration
@EnableWebMvc
@PropertySource("classpath:application-default.properties")
public class PredixWebsocketServerApplication extends SpringBootServletInitializer implements WebSocketConfigurer {

    private static final Logger LOG = Logger.getLogger(PredixWebsocketServerApplication.class.getName());

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(PredixWebsocketServerApplication.class);
        ApplicationContext ctx = springApplication.run(args);

        LOG.log(Level.INFO, "Let's inspect the beans provided by Spring Boot:");
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            LOG.log(Level.INFO, beanName);
        }

        LOG.log(Level.INFO, "Let's inspect the profiles provided by Spring Boot:");
        String profiles[] = ctx.getEnvironment().getActiveProfiles();
        for (int i = 0; i < profiles.length; i++) {
            LOG.log(Level.INFO, "profile={0}", profiles[i]);
        }

        LOG.log(Level.INFO, "Let's inspect the properties provided by Spring Boot:");
        MutablePropertySources propertySources = ((StandardServletEnvironment) ctx.getEnvironment())
                .getPropertySources();
        Iterator<org.springframework.core.env.PropertySource<?>> iterator = propertySources.iterator();
        while (iterator.hasNext()) {
            Object propertySourceObject = iterator.next();
            if (propertySourceObject instanceof org.springframework.core.env.PropertySource) {
                org.springframework.core.env.PropertySource<?> propertySource = (org.springframework.core.env.PropertySource<?>) propertySourceObject;
                LOG.log(Level.INFO, "propertySource={0} values={1}class={2}", new Object[]{propertySource.getName(), propertySource.getSource(), propertySource.getClass()});
            }
        }
    }

    /**
     * Ensure the Tomcat container comes up, not the Jetty one.
     *
     * @return - the factory
     */
    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
        return new TomcatEmbeddedServletContainerFactory();
    }

    @Bean
    public SpringEndpointConfigurator configurator() {
        return new SpringEndpointConfigurator();
    }

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        LOG.log(Level.INFO, "Configure mediator ...");
//        // TODO Auto-generated method stub
//        return builder.sources(PredixWebsocketServerApplication.class);
//    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry arg0) {
        // TODO Auto-generated method stub
        LOG.log(Level.INFO, "Mediator register websocket handlers ...");
    }

}
