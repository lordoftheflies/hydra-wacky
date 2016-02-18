/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.data.visualization;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author predix
 */
@XmlRootElement
public class SeriePoint implements Serializable{

    public SeriePoint() {
    }

    public SeriePoint(Date x, Double y) {
        this.x = x;
        this.y = y;
    }
    
    
    
    private Date x;

    public Date getX() {
        return x;
    }

    public void setX(Date x) {
        this.x = x;
    }
    
    private Double y;

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
    
}
