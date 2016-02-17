/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.hydra.datasource;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lordoftheflies
 */
@RestController
public class InfrastructureController {

    public FeatureCollection siteGeoJson(String assetId) {
        FeatureCollection dto = new FeatureCollection();

        return dto;
    }

    public Feature locationGeoJSon(String assetId) {
        Feature dto = new Feature();

        return dto;
    }
}
