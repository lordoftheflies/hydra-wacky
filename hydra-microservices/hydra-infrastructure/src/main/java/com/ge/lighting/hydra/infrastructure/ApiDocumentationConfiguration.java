/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.infrastructure;

import com.ge.lighting.hydra.infrastructure.rest.AssetTreeController;
import com.ge.lighting.hydra.infrastructure.rest.DataController;
import com.ge.lighting.hydra.infrastructure.rest.ModelController;
import com.ge.lighting.hydra.infrastructure.rest.SpatialAssetController;
import com.google.common.base.Predicate;
import static com.google.common.base.Predicates.or;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import static springfox.documentation.builders.PathSelectors.regex;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author predix
 */
@Configuration
@EnableSwagger2
@ComponentScan(basePackageClasses = {
    ModelController.class,
    AssetTreeController.class,
    SpatialAssetController.class,
    DataController.class
})
public class ApiDocumentationConfiguration {

    @Bean
    public Docket documentation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(paths())
                .build()
                .apiInfo(metadata());
    }

    //Here is an example where we select any api that matches one of these paths
    private Predicate<String> paths() {
        return or(
                regex("/model.*"),
                regex("/asset.*"),
                regex("/data.*"));
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfiguration.DEFAULT;
    }

    private ApiInfo metadata() {
        return new ApiInfoBuilder()
                .title("Hydra Infrastructure API")
                .description("This API made for visualizing data of Hydra system.")
                .version("1.0.0")
                .contact("laszlo.hegeds@ge.com")
                .build();
    }

}
