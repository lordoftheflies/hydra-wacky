/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.infrastructure.visitors;

import com.ge.lighting.hydra.asset.DataExtractorDto;
import com.ge.lighting.hydra.asset.device.entity.AssetContainerEntity;
import com.ge.lighting.hydra.asset.device.entity.AssetEntity;
import com.ge.lighting.hydra.asset.device.entity.AssetExtractorEntity;
import com.ge.lighting.hydra.Naming;
import com.ge.lighting.hydra.Visitor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.geojson.Polygon;
import org.springframework.stereotype.Component;

/**
 *
 * @author predix
 */
@Component
public class SpatialVisitor implements Visitor<DataExtractorDto> {

    public SpatialVisitor() {
    }
    
    
    private Map<String, Object> decorateWithVisual(Map<String, Object> properties, AssetExtractorEntity entity) {
        properties.put("icon", entity.getMetaEntity().getMd().getIcon());
        return properties;
    }
        
    /**
     * Add visual params to display indoor-map
     * @param properties
     * @param entity
     * @return 
     */
    private Map<String, Object> decorateWithVisual(Map<String, Object> properties, AssetContainerEntity entity) {
        
        properties.put("stroke", "#000000");
        properties.put("stroke-width", 2);
        properties.put("stroke-opacity", 0.5);
        properties.put("fill", "#80ffff");
        properties.put("fill-opacity", 0.1);
        
        properties.put("level", 0);
        
        return properties;
    }
    
    private Map<String, Object> basicProperties(AssetEntity assetEntity) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", assetEntity.getFriendlyName());
        properties.put("description", assetEntity.getDescription());
        return properties;
    }

    protected DataExtractorDto visitbase(AssetEntity assetEntity) {
        DataExtractorDto dto = new DataExtractorDto();
        
        dto.setId(assetEntity.getId().toString());
        dto.setClassification(assetEntity.getMetaEntity().getId());        
        dto.setProperties(this.basicProperties(assetEntity));
        
        return dto;
    }

    @Override
    public DataExtractorDto visit(AssetContainerEntity o) {
        DataExtractorDto dto = visitbase(o);
        this.decorateWithVisual(dto.getProperties(), o);
        
        return dto;
    }

    @Override
    public DataExtractorDto visit(AssetExtractorEntity o) {
        DataExtractorDto dto = visitbase(o);

        this.decorateWithVisual(dto.getProperties(), o);
        
        dto.setGeometry(new Point(o.getLocation().getY(), o.getLocation().getX()));

        return dto;
    }
}
