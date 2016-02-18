/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.core.visitors;

import com.ge.lighting.hydra.asset.DataExtractorDto;
import com.ge.lighting.hydra.asset.device.entity.AssetContainerEntity;
import com.ge.lighting.hydra.asset.device.entity.AssetEntity;
import com.ge.lighting.hydra.asset.device.entity.AssetExtractorEntity;
import com.ge.lighting.hydra.Naming;
import com.ge.lighting.hydra.Visitor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    protected DataExtractorDto visitbase(AssetEntity o) {
        DataExtractorDto dto = new DataExtractorDto();
        dto.setId(o.getId().toString());
//        dto.setFriendlyName(Naming.friendlyNameFactory(o));
        dto.setClassification(o.getMetaEntity().getId());
        return dto;
    }

    @Override
    public DataExtractorDto visit(AssetContainerEntity o) {
        DataExtractorDto dto = visitbase(o);

        List<LngLatAlt> coords = new ArrayList<>();
        Arrays.asList(o.getBounds().getCoordinates()).forEach(a -> coords.add(new LngLatAlt(a.x, a.y)));
        dto.setGeometry(new Polygon(coords));

        return dto;
    }

    @Override
    public DataExtractorDto visit(AssetExtractorEntity o) {
        DataExtractorDto dto = visitbase(o);
        
        dto.setGeometry(new Point(o.getLocation().getX(), o.getLocation().getY()));

        return dto;
    }
}
