package com.ge.current.innovation.hydra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = "com.ge.current.innovation")
public class HydraServerBootApplication {

    
    public static void main(String[] args) {
        SpringApplication.run(HydraServerBootApplication.class, args);
    }
}
