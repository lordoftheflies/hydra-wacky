/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.asset.model.entity;

import com.ge.lighting.hydra.asset.MetaData;
import java.util.UUID;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 *
 * @author predix
 */
@Entity
@DiscriminatorValue(value = "attribute")
public class MetaAttributeEntity extends MetaEntity {

    public MetaAttributeEntity() {
    }

    public MetaAttributeEntity(MetaTypeEntity owner, MetricEntity metric, UUID id, MetaData md) {
        super(id, md);
        this.owner = owner;
        this.metric = metric;
    }
    
    
    
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private MetaTypeEntity owner;

    public MetaTypeEntity getOwner() {
        return owner;
    }

    public void setOwner(MetaTypeEntity owner) {
        this.owner = owner;
    }
    
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private MetricEntity metric;

    public MetricEntity getMetric() {
        return metric;
    }

    public void setMetric(MetricEntity metric) {
        this.metric = metric;
    }
}
