package com.ge.lighting.hydra.mq.connector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

//@SpringBootApplication
//@Profile("cloud")
//@EnableAutoConfiguration
public class HydraConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HydraConsumerApplication.class, args);
    }
}
