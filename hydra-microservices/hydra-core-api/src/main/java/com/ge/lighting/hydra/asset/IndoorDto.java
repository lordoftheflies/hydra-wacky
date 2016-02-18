/*
 * Copyright 2015 GE Lighting EMEA Innovation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ge.lighting.hydra.asset;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.ge.lighting.innovation.persistence.ext.hibernate.json.PgGeoJsonFeatureCollection;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author predix
 */
@XmlRootElement(name = "FeatureCollection")
@JsonTypeName(value = "FeatureCollection")
public class IndoorDto extends PgGeoJsonFeatureCollection implements Serializable{

    public IndoorDto() {
    }
}