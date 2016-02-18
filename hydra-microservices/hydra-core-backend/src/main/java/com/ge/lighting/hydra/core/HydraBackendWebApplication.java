package com.ge.lighting.hydra.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Profile;

import com.ge.lighting.hydra.asset.AssetStore;
import com.ge.lighting.hydra.datastore.DataStore;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
//    "com.ge.lighting.hydra.datastore",
//    "com.ge.lighting.hydra.asset",
    "com.ge.lighting.hydra.core"
})
//@Profile({"local", "dev"})
public class HydraBackendWebApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
//    	SpringApplication.run(RestConfig.class, args);
        SpringApplication.run(HydraBackendWebApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application
//        		.sources(RestConfig.class)
//        		.sources(DataStore.class)
//        		.sources(AssetStore.class)
        		.sources(HydraBackendWebApplication.class);
    }
}
