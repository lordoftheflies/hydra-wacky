/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.infrastructure.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author predix
 */
public class AssetDetailsDto extends AssetNodeDto {

    public AssetDetailsDto() {
    }

    public AssetDetailsDto(Long id, Long parentId, Long classification, String name, String description, Map<String, Object> properties) {
        super(id, parentId, classification, name, description);
        this.properties = properties;
    }
    
    @ApiModelProperty(value = "properties", notes = "Asset attributes values.")
    private Map<String, Object> properties;

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
    
    
}
