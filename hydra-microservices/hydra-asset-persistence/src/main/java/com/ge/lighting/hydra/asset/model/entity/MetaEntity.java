/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.asset.model.entity;

import com.ge.lighting.hydra.asset.MetaData;
import com.ge.lighting.innovation.persistence.ext.AbstractEntity;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

/**
 *
 * @author predix
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@TypeDefs({
    @TypeDef(name = "MetaData", typeClass = MetaData.class)
})
public abstract class MetaEntity extends AbstractEntity<UUID> {

    public MetaEntity() {
    }

    public MetaEntity(UUID id, MetaData md) {
        this.id = id;
        this.md = md;
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

    @Column(columnDefinition = "json")
    @Type(type = "MetaData")
    protected MetaData md;

    public MetaData getMd() {
        return md;
    }

    public void setMd(MetaData md) {
        this.md = md;
    }
}
