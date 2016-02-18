/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.asset.device.entity;

import com.ge.lighting.hydra.Visitable;
import com.ge.lighting.hydra.Visitor;
import com.ge.lighting.hydra.asset.model.entity.MetaEntity;
import com.vividsolutions.jts.geom.Point;
import java.util.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Hegedűs László (212429780)
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "AssetExtractorEntity.exsists", query = "SELECT e FROM AssetExtractorEntity e WHERE e.id = :id")
})
@DiscriminatorValue("extractor")
public class AssetExtractorEntity extends AssetEntity implements Visitable {

    private static final long serialVersionUID = 1L;

    public AssetExtractorEntity() {
    }

    public AssetExtractorEntity(UUID id, String friendlyName, String description, AssetContainerEntity parent, MetaEntity metaEntity, Boolean active, Point location) {
        super(id, friendlyName, description, parent, metaEntity, active);
        this.location = location;
    }

    protected Point location;

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

//    @Override
//    public DataExtractorDto getDto() {
//        // TODO: set coordinate-reference-system.
//        // TODO: set altitude.
//        DataExtractorDto dto = new DataExtractorDto(
//                getId(),
//                getLocation().getX(),
//                getLocation().getY(),
//                0.0,
//                "luminary");
//        return dto;
//    }
    @Override
    public void visit(Visitor visitor) {
        visitor.visit(this);
    }
}
