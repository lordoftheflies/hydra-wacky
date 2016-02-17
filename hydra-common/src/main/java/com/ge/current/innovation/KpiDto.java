/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation;

import java.util.Date;

/**
 *
 * @author lordoftheflies
 */
public class KpiDto {
    
    private String assetMeterUri;
    
    private String name;
    
    private Date ts;

    private double value;

    public KpiDto() {
    }

    public KpiDto(String assetMeterUri, String name, Date ts, double value) {
        this.assetMeterUri = assetMeterUri;
        this.name = name;
        this.ts = ts;
        this.value = value;
    }
    
    public String getAssetMeterUri() {
        return assetMeterUri;
    }

    public void setAssetMeterUri(String assetMeterUri) {
        this.assetMeterUri = assetMeterUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    
}
