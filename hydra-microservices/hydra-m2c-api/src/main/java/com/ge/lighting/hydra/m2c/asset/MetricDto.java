package com.ge.lighting.hydra.m2c.asset;

import java.io.Serializable;

public class MetricDto implements Serializable {

    public MetricDto() {
    }

    public MetricDto(String id, String unit, String name, String description) {
        this.id = id;
        this.unit = unit;
        this.name = name;
        this.description = description;
    }
    
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    private String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
