/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.data.visualization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author predix
 */
@ApiModel(value = "HeatMapDto", description = "Datastructure for visualize heatmap on the map.")
@XmlRootElement
public class HeatMapDto implements Serializable {

    public HeatMapDto() {
    }

    public HeatMapDto(List<List<Object>> data) {
        this.data = data;
    }

    public HeatMapDto(HeatMapDataPoint... data) {
        this.add(data);
    }
    
    
    @ApiModelProperty(value = "data", notes = "Representing data points of the heatmap.")
    private List<List<Object>> data;

    public List<List<Object>> getData() {
        return data;
    }

    public void setData(List<List<Object>> data) {
        this.data = data;
    }

    public void add(HeatMapDataPoint... points) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        for (HeatMapDataPoint point : points) {
            this.data.add(Arrays.asList(new Object[]{
                point.getLat(),
                point.getLng(),
                point.getValue().toString()
            }));
        }
    }
}
