package com.ge.lighting.hydra.data.visualization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.ge.lighting.hydra.datastore",
    "com.ge.lighting.hydra.asset",
    "com.ge.lighting.hydra.data.visualization"
})
public class VisualizationModule extends SpringBootServletInitializer {

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        builder.sources(ProficyConnector.class);
//        return super.configure(builder); //To change body of generated methods, choose Tools | Templates.
//    }

    public static void main(String[] args) {
        SpringApplication.run(VisualizationModule.class, args);
    }
}
