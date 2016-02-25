/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.visualization.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author predix
 */
@ApiModel(value = "HeatMapDataPoint", description = "Homogen point in heatmap space.")
@XmlRootElement
public class HeatMapDataPoint implements Serializable {

    public HeatMapDataPoint() {
    }

    public HeatMapDataPoint(Double lat, Double lng, Double value) {
        this.lat = lat;
        this.lng = lng;
        this.value = value;
    }

    @ApiModelProperty(value = "lat", notes = "Latitude coordinate.")
    private Double lat;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @ApiModelProperty(value = "lng", notes = "Longitude coordinate.")
    private Double lng;

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @ApiModelProperty(value = "lat", notes = "Value of the heatmap on the coordinate latitude:longitude.")
    private Double value;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

}
