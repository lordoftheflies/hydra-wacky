package com.ge.lighting.hydra.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author predix
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.ge.lighting.hydra.infrastructure",
    "com.ge.lighting.hydra.infrastructure.rest",
    "com.ge.lighting.hydra.infrastructure.visitors",
    "com.ge.lighting.hydra.datastore",
    "com.ge.lighting.hydra.model",
    "com.ge.lighting.hydra.asset"
})
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
