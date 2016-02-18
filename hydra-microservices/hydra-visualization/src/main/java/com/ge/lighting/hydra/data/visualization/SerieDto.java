/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.data.visualization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author predix
 */
@XmlRootElement
public class SerieDto {

    public SerieDto() {
    }

    public SerieDto(String name, SeriePoint... series) {
        this.name = name;
        this.series = Arrays.asList(series);
    }

    public SerieDto(String name, List<SeriePoint> series) {
        this.name = name;
        this.series = series;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private List<SeriePoint> series;

    public List<SeriePoint> getSeries() {
        return series;
    }

    public void setSeries(List<SeriePoint> series) {
        this.series = series;
    }

    public void add(SeriePoint... points) {
        if (this.series == null) {
            this.series = new ArrayList<>();
        }
        this.series.addAll(Arrays.asList(points));
    }

    public void put(Date x, Double y) {
        if (this.series == null) {
            this.series = new ArrayList<>();
        }
        this.series.add(new SeriePoint(x, y));
    }
}
