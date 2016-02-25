/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.hydra.storage.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author lordoftheflies
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "DataPointEntity.findSeriesFrom", query = "SELECT p FROM DataPointEntity p WHERE (p.assetMeter.id = :key AND p.timeStamp >= :begin)"),
    @NamedQuery(name = "DataPointEntity.findSeriesFromLocation", query = "SELECT p FROM DataPointEntity p WHERE (p.assetMeter.id = :key AND p.timeStamp >= :begin AND p.assetMeter.asset.id = :locationId)"),
    @NamedQuery(name = "DataPointEntity.findSeries", query = "SELECT p FROM DataPointEntity p WHERE (p.assetMeter.id = :key AND p.timeStamp >= :begin AND p.timeStamp <= :end AND p.assetMeter.asset.id = :locationId)"),
    @NamedQuery(name = "DataPointEntity.findSeriesEverywhere", query = "SELECT p FROM DataPointEntity p WHERE (p.assetMeter.id = :key AND p.timeStamp >= :begin AND p.timeStamp <= :end)")
})
public class DataPointEntity implements Serializable {

    public DataPointEntity() {
    }

    public DataPointEntity(Long id, Double val, AssetMeterEntity assetMeter, Date timeStamp) {
        this.id = id;
        this.val = val;
        this.assetMeter = assetMeter;
        this.timeStamp = timeStamp;
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
    
    @Basic
    private Double val;

    public Double getVal() {
        return val;
    }

    public void setVal(Double value) {
        this.val = value;
    }
    
    @ManyToOne
    private AssetMeterEntity assetMeter;

    public AssetMeterEntity getAssetMeter() {
        return assetMeter;
    }

    public void setAssetMeter(AssetMeterEntity assetMeter) {
        this.assetMeter = assetMeter;
    }
    
    @Basic
    @Temporal(TemporalType.DATE)
    private Date timeStamp;

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
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
        if (!(object instanceof DataPointEntity)) {
            return false;
        }
        DataPointEntity other = (DataPointEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ge.current.innovation.hydra.storage.message.entities.MessageEntity[ id=" + id + " ]";
    }
    
}
