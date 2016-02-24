package com.ge.current.innovation.hydra.dataingestion;

import com.ge.current.innovation.hydra.storage.MessageStoreLocalConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {
    MessageStoreLocalConfiguration.class,
    DataIngestionConfiguration.class
})
public class DataingestionServiceApplication {

    
    
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(DataingestionServiceApplication.class, args);
    }

    
}
