package com.ge.lighting.hydra.asset;

import java.io.Serializable;
import java.util.UUID;

public class MetaAttributeDto implements Serializable {

    public MetaAttributeDto() {
    }

    public MetaAttributeDto(String key, String unit, String name, String description) {
        this.key = key;
        this.unit = unit;
        this.name = name;
        this.description = description;
    }
    
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
