/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.m2c.asset;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import org.geojson.FeatureCollection;

/**
 *
 * @author Hegedűs László (212429780)
 */
@XmlRootElement(name = "FeatureCollection")
@JsonTypeName(value = "FeatureCollection")
public class InfraDto extends FeatureCollection implements Serializable{

    public InfraDto() {
    }
    
}
