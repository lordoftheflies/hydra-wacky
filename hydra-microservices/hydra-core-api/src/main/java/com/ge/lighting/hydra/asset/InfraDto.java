/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.asset;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.ge.lighting.innovation.persistence.ext.hibernate.json.PgGeoJsonFeatureCollection;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Hegedűs László (212429780)
 */
@XmlRootElement(name = "FeatureCollection")
@JsonTypeName(value = "FeatureCollection")
public class InfraDto extends PgGeoJsonFeatureCollection implements Serializable{

    public InfraDto() {
    }
}
