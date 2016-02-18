/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.infrastructure.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.UUID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author predix
 */
@ApiModel(value = "asset", description = "Asset information.")
@XmlRootElement
public class AssetNodeDto implements Serializable {

    public AssetNodeDto() {
    }

    public AssetNodeDto(UUID id, UUID parentId, String classificationKey, String classificationId, String name, String description) {
        this.id = id;
        this.classification = classificationKey;
        this.classificationId = classificationId;
        this.name = name;
        this.description = description;
    }

    @ApiModelProperty(value = "id", notes = "Asset id.")
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @ApiModelProperty(value = "classification", notes = "Classification key.")
    private String classification;

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @ApiModelProperty(value = "classificationId", notes = "Classification identifier.")
    private String classificationId;

    public String getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
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
    
    @ApiModelProperty(value = "parentId", notes = "Id of the parent element.")
    private UUID parentId;

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
}
