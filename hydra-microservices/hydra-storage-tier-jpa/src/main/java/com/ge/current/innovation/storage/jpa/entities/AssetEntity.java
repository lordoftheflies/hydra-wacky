/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.storage.jpa.entities;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author lordoftheflies
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "AssetEntity.findRoots", query = "SELECT a FROM AssetEntity a WHERE a.parent IS NULL"),
    @NamedQuery(name = "AssetEntity.findChildren", query = "SELECT a FROM AssetEntity a WHERE a.parent.id = :parentId")
})
public class AssetEntity extends DomainEntity {

    public AssetEntity() {
    }

    public AssetEntity(String uri, String friendlyName, String description) {
        super(uri, friendlyName, description);
    }

    @OneToMany(mappedBy = "parent")
    private List<AssetEntity> children;

    public List<AssetEntity> getChildren() {
        return children;
    }

    public void setChildren(List<AssetEntity> children) {
        this.children = children;
    }

    private String nodeId;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
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

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private ClassificationEntity classification;

    public ClassificationEntity getClassification() {
        return classification;
    }

    public void setClassification(ClassificationEntity classification) {
        this.classification = classification;
    }

    @ManyToOne
    private AssetEntity parent;

    public AssetEntity getParent() {
        return parent;
    }

    public void setParent(AssetEntity parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "asset")
    private List<AssetMeterEntity> assetMeters;

    public List<AssetMeterEntity> getAssetMeters() {
        return assetMeters;
    }

    public void setAssetMeters(List<AssetMeterEntity> assetMeters) {
        this.assetMeters = assetMeters;
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
        if (!(object instanceof AssetEntity)) {
            return false;
        }
        AssetEntity other = (AssetEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ge.current.innovation.hydra.storage.entities.AssetEntity[ id=" + id + " ]";
    }

}
