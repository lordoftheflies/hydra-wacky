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
public class DataFilter {

    private String assetUri;

    private String meterKey;

    private Date begin;

    private Date end;

    public DataFilter() {
    }

    public DataFilter(String assetUri, String meterKey, Date begin, Date end) {
        this.assetUri = assetUri;
        this.meterKey = meterKey;
        this.begin = begin;
        this.end = end;
    }

    public String getAssetUri() {
        return assetUri;
    }

    public void setAssetUri(String assetUri) {
        this.assetUri = assetUri;
    }

    public String getMeterKey() {
        return meterKey;
    }

    public void setMeterKey(String meterKey) {
        this.meterKey = meterKey;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "DataFilter{" + "assetUri=" + assetUri + ", meterKey=" + meterKey + ", begin=" + begin + ", end=" + end + '}';
    }

}
