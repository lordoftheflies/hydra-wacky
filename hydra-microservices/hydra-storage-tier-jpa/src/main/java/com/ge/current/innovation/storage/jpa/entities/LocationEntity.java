/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.storage.jpa.entities;

import com.ge.current.innovation.storage.jpa.boot.FeatureCollectionGeoJsonUserType;
import com.ge.current.innovation.storage.jpa.boot.FeatureGeoJsonUserType;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.geojson.Feature;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

/**
 *
 * @author lordoftheflies
 */
@Entity
@TypeDefs({
    @TypeDef(name = "FeatureGeoJson", typeClass = FeatureGeoJsonUserType.class)
})
@NamedQueries({
    @NamedQuery(name = "LocationEntity.findByParent", query = "SELECT e FROM LocationEntity e WHERE e.parent.id = :parentId")
})
public class LocationEntity extends GeoJsonEntity<Feature> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    private SiteEntity parent;

    public SiteEntity getParent() {
        return parent;
    }

    public void setParent(SiteEntity parent) {
        this.parent = parent;
    }

    @Type(type = "FeatureGeoJson")
    @Basic(optional = true)
    private Feature geoJson;

    @Override
    public Feature getGeoJson() {
        return geoJson;
    }

    @Override
    public void setGeoJson(Feature geoJson) {
        this.geoJson = geoJson;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LocationEntity)) {
            return false;
        }
        LocationEntity other = (LocationEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ge.current.innovation.storage.jpa.entities.LocationEntity[ id=" + id + " ]";
    }

}
