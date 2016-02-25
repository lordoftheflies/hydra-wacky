/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.infrastructure.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author predix
 */
@ApiModel(value = "classification", description = "Asset classification.")
@XmlRootElement
public class ClassificationDto extends ContentLikeDto {

    public ClassificationDto() {
    }

    public ClassificationDto(Long id, String name, String description, AttributeDto... attributes) {
        super(id, name, description);
        this.attributes = Arrays.asList(attributes);
    }

    public ClassificationDto(Long id, String name, String description, Collection<AttributeDto> attributes) {
        super(id, name, description);
        this.attributes = new ArrayList<>(attributes);
    }
    
    

    @ApiModelProperty(value = "attributes", notes = "Attributes of the classification.")
    private List<AttributeDto> attributes;

    public List<AttributeDto> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeDto> attributes) {
        this.attributes = attributes;
    }

    
    
    

}
