package com.ge.lighting.hydra.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@SpringBootApplication
//@EnableAutoConfiguration
//@Profile({"cloud"})
public class HydraBackendApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(HydraBackendApplication.class, args);
    }
}
