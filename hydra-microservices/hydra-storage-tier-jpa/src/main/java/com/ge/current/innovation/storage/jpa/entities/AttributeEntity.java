/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.storage.jpa.entities;

import java.io.Serializable;
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
    @NamedQuery(name = "AttributeEntity.findByClassification", query = "SELECT a FROM AttributeEntity a WHERE a.classification.id = :classificationId")
})
public class AttributeEntity extends DomainEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public AttributeEntity() {
    }

    public AttributeEntity(String uri, String friendlyName, String description) {
        super(uri, friendlyName, description);
    }
    
    
    
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
    private ClassificationEntity classification;

    public ClassificationEntity getClassification() {
        return classification;
    }

    public void setClassification(ClassificationEntity classification) {
        this.classification = classification;
    }
    
    @OneToMany(mappedBy = "attribute")
    private List<AssetMeterEntity> meters;

    public List<AssetMeterEntity> getMeters() {
        return meters;
    }

    public void setMeters(List<AssetMeterEntity> meters) {
        this.meters = meters;
    }
    
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private MeterEntity meter;

    public MeterEntity getMeter() {
        return meter;
    }

    public void setMeter(MeterEntity meter) {
        this.meter = meter;
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
        if (!(object instanceof AttributeEntity)) {
            return false;
        }
        AttributeEntity other = (AttributeEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ge.current.innovation.hydra.storage.entities.AttributeEntity[ id=" + id + " ]";
    }
    
}
