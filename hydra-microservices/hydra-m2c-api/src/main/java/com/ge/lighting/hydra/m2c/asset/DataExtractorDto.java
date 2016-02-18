/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.m2c.asset;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.geojson.Feature;
import org.geojson.Point;

/**
 *
 * @author Hegedűs László (212429780)
 */
@XmlRootElement(name = "Feature")
@JsonTypeName(value = "Feature")
public class DataExtractorDto extends Feature implements Serializable {

    @XmlElement(nillable = true)
    private MetaData metadata;

    public DataExtractorDto() {
        this.sensors = new ArrayList<>();
        this.setGeometry(new Point());
//        this.init();
    }

    public DataExtractorDto(UUID id, double lon, double lat, double alt, String classification) {
        this.sensors = new ArrayList<>();
        this.setId(id.toString());
        this.setGeometry(new Point(lat, lon, alt));
//        this.init();
    }
    
//    private void init() {
//        if (this.classification == null) this.classification = "asset";
//        if (this.friendlyName == null) {
//            this.friendlyName = this.classification.substring(0, 1).toUpperCase()
//                    + this.classification.substring(1).toLowerCase()
//                    + this.getId().hashCode();
//        }
//    }

    private String friendlyName;

    public String getFriendlyName() {
        
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    private String classification;

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public MetaData getMetadata() {
        return metadata;
    }

    public void setMetadata(MetaData metadata) {
        this.metadata = metadata;
    }

    private List<SensorDataDto> sensors;

    public List<SensorDataDto> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorDataDto> sensors) {
        this.sensors = sensors;
    }
}
