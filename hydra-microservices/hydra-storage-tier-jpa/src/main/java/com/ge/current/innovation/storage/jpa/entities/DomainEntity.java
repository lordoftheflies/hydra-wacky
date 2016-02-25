/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.storage.jpa.entities;

import java.io.Serializable;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author lordoftheflies
 */
@MappedSuperclass
public class DomainEntity implements Serializable {

    public DomainEntity(String uri, String friendlyName, String description) {
        this.uri = uri;
        this.friendlyName = friendlyName;
        this.description = description;
    }

    public DomainEntity() {
    }

    
    
    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    private String friendlyName;

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
