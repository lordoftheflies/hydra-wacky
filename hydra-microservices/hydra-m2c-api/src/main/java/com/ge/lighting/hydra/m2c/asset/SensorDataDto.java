/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.m2c.asset;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author predix
 */
@XmlRootElement
public class SensorDataDto extends MetricDto implements Serializable {

    public SensorDataDto() {
    }

    public SensorDataDto(MetricDto metric, double value) {
        this.setId(metric.getId());
        this.setName(metric.getName());
        this.setDescription(metric.getDescription());
        this.setUnit(metric.getUnit());
        this.value = value;
    }

    private Double value;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
