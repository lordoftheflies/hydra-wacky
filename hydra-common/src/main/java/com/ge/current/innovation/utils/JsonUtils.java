/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 *
 * @author lordoftheflies
 */
public class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    public <T> T read(Class<T> rootEntityType, String msg) throws IOException {
        return mapper.readValue(msg, rootEntityType);
    }
    
    public <T> String write(T entity, String msg) throws IOException {
        return mapper.writeValueAsString(entity);
    }
}
