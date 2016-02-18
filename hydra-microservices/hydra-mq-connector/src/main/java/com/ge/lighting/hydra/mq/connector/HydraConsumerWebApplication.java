package com.ge.lighting.hydra.mq.connector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
//@EnableAutoConfiguration
@ComponentScan(basePackages = {
	    "com.ge.lighting.hydra.datastore",
		"com.ge.lighting.hydra.model",
	    "com.ge.lighting.hydra.asset",
	    "com.ge.lighting.hydra.mq.connector",
	    "com.ge.lighting.hydra.mq.connector.rabbitmq"
	})
@Profile({"local"})
public class HydraConsumerWebApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(HydraConsumerWebApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }

    private static Class<HydraConsumerWebApplication> applicationClass = HydraConsumerWebApplication.class;
}
