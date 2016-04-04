package com.ge.current.innovation.dataingestion.consumer.boot;

import com.ge.current.innovation.dataingestion.consumer.listeners.DataIngestionReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication // the annotation replaces @Configuration, @EnableAutoConfiguration, @ComponentScan
@ComponentScan(basePackages = "com.ge.current.innovation")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
//    @Autowired
//    private DataIngestionReceiver dataStreamReceiver;
}
