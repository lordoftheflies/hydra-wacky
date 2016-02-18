/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.infrastructure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author predix
 */
@Configuration
@ComponentScan({
    "com.ge.lighting.hydra.infrastucture.rest"
})
//@EnableWebMvc
public class RestConfig extends WebMvcConfigurerAdapter {

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
//        stringConverter.setWriteAcceptCharset(false);
//        converters.add(new ByteArrayHttpMessageConverter());
//        converters.add(stringConverter);
//        converters.add(new ResourceHttpMessageConverter());
//        converters.add(new SourceHttpMessageConverter<>());
//        converters.add(new AllEncompassingFormHttpMessageConverter());
//        converters.add(jackson2Converter());
//    }
//
//    @Bean
//    public MappingJackson2HttpMessageConverter jackson2Converter() {
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        converter.setObjectMapper(objectMapper());
//        return converter;
//    }
//
//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//        return objectMapper;
//    }
}
