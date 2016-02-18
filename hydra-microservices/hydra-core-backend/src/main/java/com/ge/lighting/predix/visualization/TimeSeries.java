/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.predix.visualization;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import static com.vividsolutions.jts.geom.Dimension.A;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author predix
 */
public class TimeSeries implements Serializable {

    public TimeSeries() {
        this.labels = new ArrayList<>();
        this.series = new ArrayList<>();
        this.data = new ArrayList<>();
    }

    private List<String> labels;

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
    
    private List<String>  series;

    public List<String> getSeries() {
        return series;
    }

    public void setSeries(List<String> series) {
        this.series = series;
    }
    
    private List<List<Double>> data;

    public List<List<Double>> getData() {
        return data;
    }

    public void setData(List<List<Double>> data) {
        this.data = data;
    }

    public void pushData(Date ts, double... values) {
        this.labels.add(ts.toString());
        int i = 0;
        for (List<Double> s : this.data) {
            s.add(values[i++]);
        }
    }
    
    public void pushData(Date ts, int keyIndex, double value) {
        this.labels.add(ts.toString());
        this.data.get(keyIndex).add(value);
    }
    
    public void newSerie(String name) {
        this.series.add(name);
        this.data.add(new ArrayList<>());
    }

    public void pushData(Date ts, String key, Double value) {
        int index = this.series.indexOf(key);
        if (index == -1) {
            this.newSerie(key);
            index = this.data.size() - 1;
        }
        if (!this.labels.contains(ts.toString())) this.labels.add(ts.toString());
        this.data.get(index).add(value);
    }
}
