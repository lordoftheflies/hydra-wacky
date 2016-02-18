/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.datastore.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Type;

/**
 *
 * @author Hegedűs László (212429780)
 */
@Embeddable
public class DataKey implements Serializable {

    private String xType;
    @Temporal(TemporalType.TIMESTAMP)
    private Date ts;
    @Column(columnDefinition = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID asset;

    public DataKey() {
    }

    public DataKey(DataKey key) {
        this.xType = key.getxType();
        this.ts = key.getTs();
        this.asset = key.getAsset();
    }

    public DataKey(String xType, Date ts, UUID asset) {
        this.xType = xType;
        this.ts = ts;
        this.asset = asset;
    }

    public String getxType() {
        return xType;
    }

    public void setxType(String xType) {
        this.xType = xType;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public UUID getAsset() {
        return asset;
    }

    public void setAsset(UUID asset) {
        this.asset = asset;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof DataKey) {
            return this.hashCode() == obj.hashCode();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return asset.hashCode() + ts.hashCode() + xType.hashCode();
    }

    @Override
    public String toString() {
        return "[" + xType + "|" + ts + "|" + asset + ']';
    }

}
