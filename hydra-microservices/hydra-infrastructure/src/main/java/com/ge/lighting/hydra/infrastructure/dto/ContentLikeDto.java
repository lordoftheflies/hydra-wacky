/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.infrastructure.dto;

import com.ge.lighting.hydra.asset.MetaData;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author predix
 */
public class ContentLikeDto implements Serializable {

    public ContentLikeDto() {
    }

    public ContentLikeDto(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @ApiModelProperty(value = "id", notes = "Id of the model.")
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    
    @ApiModelProperty(value = "name", notes = "Display name of the asset.")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ApiModelProperty(value = "description", notes = "Description of the asset.")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private MetaData meta;

    public MetaData getMeta() {
        return meta;
    }

    public void setMeta(MetaData meta) {
        this.meta = meta;
    }
}
