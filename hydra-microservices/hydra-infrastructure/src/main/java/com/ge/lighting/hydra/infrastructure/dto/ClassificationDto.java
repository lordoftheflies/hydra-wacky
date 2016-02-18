/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.infrastructure.dto;

import com.ge.lighting.hydra.asset.MetaData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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

    public ClassificationDto(UUID id, String name, String description, AttributeDto... attributes) {
        super(id, name, description);
        this.attributes = Arrays.asList(attributes);
    }

    public ClassificationDto(UUID id, String name, String description, List<AttributeDto> attributes) {
        super(id, name, description);
        this.attributes = attributes;
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
