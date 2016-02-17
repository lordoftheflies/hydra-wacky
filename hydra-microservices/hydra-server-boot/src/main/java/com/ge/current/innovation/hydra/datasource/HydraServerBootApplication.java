package com.ge.current.innovation.hydra.datasource;

import com.ge.current.innovation.hydra.storage.MessageStoreLocalConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {
    MessageStoreLocalConfiguration.class,
    SwaggerConfiguration.class,
    InfrastructureController.class,
    VisualizationController.class,
    ViewsController.class
})
public class HydraServerBootApplication {

    
    public static void main(String[] args) {
        SpringApplication.run(HydraServerBootApplication.class, args);
    }
}
