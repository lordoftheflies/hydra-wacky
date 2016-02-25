package com.ge.current.innovation.storage.jpa.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.ge.current.innovation.storage.jpa")
public class HydraStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(HydraStorageApplication.class, args);
    }
}
