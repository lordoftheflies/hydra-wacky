/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.asset.model.entity;

import com.ge.lighting.hydra.asset.MetaData;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 *
 * @author predix
 */
@Entity
@DiscriminatorValue(value = "classification")
public class MetaTypeEntity extends MetaEntity {

    public MetaTypeEntity() {
    }

    public MetaTypeEntity(UUID id, MetaData md, List<MetaAttributeEntity> attributes) {
        super(id, md);
        this.attributes = attributes;
    }
    public MetaTypeEntity(UUID id, MetaData md, MetaAttributeEntity... attributes) {
        super(id, md);
        this.attributes = Arrays.asList(attributes);
    }

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<MetaAttributeEntity> attributes;

    public List<MetaAttributeEntity> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<MetaAttributeEntity> attributes) {
        this.attributes = attributes;
    }
}
