/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.infrastructure.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author predix
 */
@ApiModel(value = "attribute", description = "Asset attribute.")
@XmlRootElement
public class AttributeDto extends ContentLikeDto {
    
    public AttributeDto() {
    }

    public AttributeDto(Long id, String key, String name, String description, String unit) {
        super(id, name, description);
        this.unit = unit;
        this.key = key;
    }
    
    @ApiModelProperty(value = "unit", notes = "Unit of the attribute.")
    private String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    @ApiModelProperty(value = "key", notes = "Key of the attribute.")
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
