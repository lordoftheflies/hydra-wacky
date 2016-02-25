/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.storage.jpa.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author lordoftheflies
 */
@Entity
public class ClassificationEntity extends DomainEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public ClassificationEntity() {
    }

    public ClassificationEntity(String uri, String friendlyName, String description) {
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

    @OneToMany(mappedBy = "classification")
    private List<AssetEntity> elements;

    public List<AssetEntity> getElements() {
        return elements;
    }

    public void setElements(List<AssetEntity> elements) {
        this.elements = elements;
    }

    @OneToMany(mappedBy = "classification")
    private List<AttributeEntity> attributes;

    public List<AttributeEntity> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeEntity> attributes) {
        this.attributes = attributes;
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
        if (!(object instanceof ClassificationEntity)) {
            return false;
        }
        ClassificationEntity other = (ClassificationEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ge.current.innovation.hydra.storage.entities.ClassificationEntity[ id=" + id + " ]";
    }

}
