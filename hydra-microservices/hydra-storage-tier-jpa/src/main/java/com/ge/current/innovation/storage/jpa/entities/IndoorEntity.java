/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.storage.jpa.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author lordoftheflies
 */
@Entity
@DiscriminatorValue(value = "indoor")
@NamedQueries({
    @NamedQuery(name = "IndoorEntity.findByStructureId", query = "SELECT e FROM IndoorEntity e WHERE e.structureId = :structureId")
})
public class IndoorEntity extends SiteEntity {
    
}
