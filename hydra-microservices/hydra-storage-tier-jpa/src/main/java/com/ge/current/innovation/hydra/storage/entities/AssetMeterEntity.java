/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.hydra.storage.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author lordoftheflies
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "AssetMeterEntity.findByUri", query = "SELECT ame FROM AssetMeterEntity ame WHERE ame.uri = :assetMeterUri"),
    @NamedQuery(name = "AssetMeterEntity.uriExists", query = "SELECT CASE WHEN (COUNT(ame) = 1) THEN true ELSE FALSE END FROM AssetMeterEntity ame WHERE ame.uri = :assetMeterUri")
})
public class AssetMeterEntity extends DomainEntity {

    public AssetMeterEntity() {
    }

    public AssetMeterEntity(String uri, String friendlyName, String description) {
        super(uri, friendlyName, description);
    }

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
    private AttributeEntity attribute;

    public AttributeEntity getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributeEntity meter) {
        this.attribute = meter;
    }

    @ManyToOne
    private AssetEntity asset;

    public AssetEntity getAsset() {
        return asset;
    }

    public void setAsset(AssetEntity asset) {
        this.asset = asset;
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
        if (!(object instanceof AssetMeterEntity)) {
            return false;
        }
        AssetMeterEntity other = (AssetMeterEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ge.current.innovation.hydra.storage.entities.AssetMeterEntity[ id=" + id + " ]";
    }

}
