/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.asset.device.entity;

import com.ge.lighting.hydra.Visitor;
import com.ge.lighting.hydra.asset.IndoorDto;
import com.ge.lighting.hydra.asset.MetaData;
import com.ge.lighting.hydra.asset.model.entity.MetaEntity;
import com.vividsolutions.jts.geom.Polygon;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

/**
 *
 * @author predix
 */
@Entity
@DiscriminatorValue("container")
@TypeDefs({
    @TypeDef(name = "IndoorDto", typeClass = IndoorDto.class)
})
public class AssetContainerEntity extends AssetEntity{

    public AssetContainerEntity() {
    }

    public AssetContainerEntity(UUID id, String friendlyName, String description, AssetContainerEntity parent, MetaEntity metaEntity, Boolean active) {
        super(id, friendlyName, description, parent, metaEntity, active);
    }
    
    @Column(columnDefinition = "json")
    @Type(type = "IndoorDto")
    private IndoorDto geoJson;

    public IndoorDto getGeoJson() {
        return geoJson;
    }

    public void setGeoJson(IndoorDto geoJson) {
        this.geoJson = geoJson;
    }
    
    
    @Basic(optional = true)
//    @Type(type = "org.hibernate.spatial.GeometryType")
//    @Column(name = "bounds", columnDefinition="Geometry", nullable = true) 
    protected Polygon bounds;

    public Polygon getBounds() {
        return bounds;
    }

    public void setBounds(Polygon bounds) {
        this.bounds = bounds;
    }
    
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<AssetEntity> children;

    public List<AssetEntity> getChildren() {
        return children;
    }

    public void setChildren(List<AssetEntity> children) {
        this.children = children;
    }

    @Override
    public void visit(Visitor v) {
        v.visit(this);
    }
    
    
}
