/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.datastore.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

/**
 * This is the main mapping class to our key-value store implemetation.
 * The entity associate OLAP dimensions to the data.
 * 
 * @author Hegedűs László (212429780)
 * 
 * @param <T> Value class template
 */
@MappedSuperclass
public abstract class DataEntryEntity<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    public DataEntryEntity() {
        super();
        // TODO Auto-generated constructor stub
    }

    public DataEntryEntity(DataKey id, T val) {
        super();
        this.id = id;
        this.val = val;
    }

    @EmbeddedId
    private DataKey id;

    public DataKey getId() {
        return id;
    }

    public void setId(DataKey id) {
        this.id = id;
    }

    @Basic(optional = true)
    @Column(name = "val")
    private T val;

    public T getVal() {
        return val;
    }

    public void setVal(T data) {
        this.val = data;
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
        if (!(object instanceof DataEntryEntity)) {
            return false;
        }
        DataEntryEntity other = (DataEntryEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ge.lighting.uranos.datastore.persistence.entities.DataEntry[ id=" + id + " ]";
    }

}
