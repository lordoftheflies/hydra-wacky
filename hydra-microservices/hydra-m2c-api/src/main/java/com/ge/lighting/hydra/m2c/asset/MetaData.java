/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.m2c.asset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Hegedűs László (212429780)
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaData implements Serializable {

    private String icon;

    public MetaData() {
    }

    public MetaData(String icon, String state, String label) {
        this.icon = icon;
        this.state = state;
        this.label = label;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private List<MetaData> subitems;

    public List<MetaData> getSubitems() {
        if (this.subitems == null) {
            this.subitems = new ArrayList<>();
        }
        return subitems;
    }

    public void setSubitems(List<MetaData> subitems) {
        this.subitems = subitems;
    }
}
