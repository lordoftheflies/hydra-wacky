package com.ge.lighting.hydra.asset.model.entity;

import com.ge.lighting.hydra.asset.MetaAttributeDto;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.ge.lighting.innovation.persistence.ext.AbstractEntity;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.PrePersist;

/**
 * Hospital entity bean
 *
 */
@Entity
public class MetricEntity extends AbstractEntity<String> {

    private static final long serialVersionUID = 1L;

    public MetricEntity() {
    }

    public MetricEntity(String id, String defaultUnitName) {
        this.id = id;
        this.defaultUnitName = defaultUnitName;
    }

    @Id
    @Basic
    private String id;

    @PrePersist
    protected void generateGuid() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString().replace("-", "_");
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String key) {
        this.id = key;
    }

    @Basic
    private String defaultUnitName;

    public String getDefaultUnitName() {
        return defaultUnitName;
    }

    public void setDefaultUnitName(String defaultUnitName) {
        this.defaultUnitName = defaultUnitName;
    }

    /**
     * @return hospital dates
     */
//    @Override
    public MetaAttributeDto getDto() {
        MetaAttributeDto dto = new MetaAttributeDto();
        dto.setKey(getId());
        dto.setUnit(getDefaultUnitName());
        
        return dto;
    }
}
