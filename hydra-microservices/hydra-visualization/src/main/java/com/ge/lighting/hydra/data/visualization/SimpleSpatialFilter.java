/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.data.visualization;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author predix
 */
public class SimpleSpatialFilter {

    public SimpleSpatialFilter(String key, Date begin, Date end, UUID... assets) {
        this.key = key;
        this.begin = begin;
        this.end = end;
        this.assets = assets;
    }

    public SimpleSpatialFilter() {
    }
    
    
    
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    private Date begin;

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }
    
    private Date end;

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
    
    private UUID[] assets;

    public UUID[] getAssets() {
        return assets;
    }

    public void setAssets(UUID[] assets) {
        this.assets = assets;
    }
}
