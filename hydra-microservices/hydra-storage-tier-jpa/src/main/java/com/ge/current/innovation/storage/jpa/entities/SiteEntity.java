/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.storage.jpa.entities;

import com.ge.current.innovation.storage.jpa.boot.FeatureCollectionGeoJsonUserType;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import org.geojson.FeatureCollection;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

/**
 *
 * @author lordoftheflies
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "aspect")
@TypeDefs({ @TypeDef(name = "FeatureCollectionGeoJson", typeClass = FeatureCollectionGeoJsonUserType.class) })
public abstract class SiteEntity extends GeoJsonEntity<FeatureCollection> implements Serializable {

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
    
    
    @OneToMany(mappedBy = "parent")
    private List<LocationEntity> locations;

    public List<LocationEntity> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationEntity> locations) {
        this.locations = locations;
    }
    
    @Type(type = "FeatureCollectionGeoJson")
    @Basic(optional = true)
    private FeatureCollection geoJson;

    @Override
    public FeatureCollection getGeoJson() {
        return geoJson;
    }

    @Override
    public void setGeoJson(FeatureCollection geoJson) {
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
        if (!(object instanceof SiteEntity)) {
            return false;
        }
        SiteEntity other = (SiteEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ge.current.innovation.storage.jpa.entities.SiteEntity[ id=" + id + " ]";
    }
    
}
