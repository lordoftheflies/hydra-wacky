/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.infrastructure.dto;

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

    public AssetNodeDto(Long id, Long parentId, Long classificationId, String name, String description) {
        this.id = id;
        this.classification = classificationId;
        this.name = name;
        this.description = description;
    }

    @ApiModelProperty(value = "id", notes = "Asset id.")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ApiModelProperty(value = "classification", notes = "Classification identifier.")
    private Long classification;

    public Long getClassification() {
        return classification;
    }

    public void setClassification(Long classification) {
        this.classification = classification;
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
