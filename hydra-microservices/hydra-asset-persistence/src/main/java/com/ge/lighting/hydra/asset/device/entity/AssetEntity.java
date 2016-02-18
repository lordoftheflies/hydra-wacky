/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.asset.device.entity;

import com.ge.lighting.hydra.Visitable;
import com.ge.lighting.hydra.asset.model.entity.MetaEntity;
import com.ge.lighting.innovation.persistence.ext.AbstractEntity;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import org.hibernate.annotations.Type;

/**
 *
 * @author predix
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "classification")
public abstract class AssetEntity extends AbstractEntity<UUID> implements Visitable {

    public AssetEntity() {
    }

    public AssetEntity(UUID id, String friendlyName, String description, AssetContainerEntity parent, MetaEntity metaEntity, Boolean active) {
        this.id = id;
        this.friendlyName = friendlyName;
        this.description = description;
        this.parent = parent;
        this.metaEntity = metaEntity;
        this.active = active;
    }

    @Id
    @Column(name = "id", columnDefinition = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    protected UUID id;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @PrePersist
    protected void generateGuid() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    @Basic
    private String friendlyName;

    public String getFriendlyName() {
        if (this.friendlyName == null) {
            return this.getMetaEntity().getMd().getLabel() + " #" + Math.abs(this.getId().hashCode());
        } else {
            return friendlyName;
        }
    }

    public void setFriendlyName(String fiendlyName) {
        this.friendlyName = fiendlyName;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private AssetContainerEntity parent;

    public AssetContainerEntity getParent() {
        return parent;
    }

    public void setParent(AssetContainerEntity parent) {
        this.parent = parent;
    }

    @ManyToOne
    private MetaEntity metaEntity;

    public MetaEntity getMetaEntity() {
        return metaEntity;
    }

    public void setMetaEntity(MetaEntity metaEntity) {
        this.metaEntity = metaEntity;
    }

    @Basic
    private Boolean active;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}
