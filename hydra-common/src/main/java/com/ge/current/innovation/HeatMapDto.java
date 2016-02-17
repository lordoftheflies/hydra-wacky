/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation;

import java.util.List;

/**
 *
 * @author lordoftheflies
 */
public class HeatMapDto {

    private double radius;

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    private List<Double[]> points;

    public List<Double[]> getPoints() {
        return points;
    }

    public void setPoints(List<Double[]> points) {
        this.points = points;
    }

}
