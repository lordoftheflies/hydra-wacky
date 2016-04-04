/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.storage.jpa.entities;

import com.ge.current.innovation.storage.jpa.boot.FeatureCollectionGeoJsonUserType;
import javax.persistence.Basic;
import javax.persistence.MappedSuperclass;
import org.geojson.GeoJsonObject;
import org.hibernate.annotations.Type;

/**
 *
 * @author lordoftheflies
 */
@MappedSuperclass
public abstract class GeoJsonEntity<GeoJsonType extends GeoJsonObject> {

    @Basic
    private String structureId;

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public abstract GeoJsonType getGeoJson();

    public abstract void setGeoJson(GeoJsonType geoJson);
}
