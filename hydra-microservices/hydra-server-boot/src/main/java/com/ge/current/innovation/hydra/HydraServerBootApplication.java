package com.ge.current.innovation.hydra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = "com.ge.current.innovation.storage.jpa")
@PropertySource("classpath:application-default.properties")
public class HydraServerBootApplication {

    
    public static void main(String[] args) {
        SpringApplication.run(HydraServerBootApplication.class, args);
    }
}
