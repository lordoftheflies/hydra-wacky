/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.infrastructure.rest;

import com.ge.lighting.hydra.asset.AssetStore;
import com.ge.lighting.hydra.asset.DataExtractorDto;
import com.ge.lighting.hydra.asset.InfraDto;
import com.ge.lighting.hydra.asset.MetaAttributeDto;
import com.ge.lighting.hydra.asset.ModelStore;
import com.ge.lighting.hydra.asset.device.entity.AssetContainerEntity;
import com.ge.lighting.hydra.asset.device.entity.AssetEntity;
import com.ge.lighting.hydra.infrastructure.visitors.DetailsVisitor;
import com.ge.lighting.hydra.infrastructure.visitors.SpatialVisitor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Backend API for visualizing asset metadata in maps.
 *
 * @author Hegedűs László (212429780)
 */
@Api(value = "SpatialAssetController")
@RestController
@RequestMapping("/assetgeojson")
public class SpatialAssetController {

    private static final String FILL_OPACITY_KEY = "fill-opacity";
    private static final String FILL_KEY = "fill";
    private static final String STROKE_OPACITY_KEY = "stroke-opacity";
    private static final String STROKE_WIDTH_KEY = "stroke-width";
    private static final String STROKE_COLOR_KEY = "stroke";

    private static final int STROKE_WIDTH = 2;
    private static final double STROKE_OPACITY = 0.5;
    private static final double FILL_OPACITY = 0.1;
    private static final String FILL_COLOR = "#80ffff";
    private static final String STROKE_COLOR = "#000000";

    private static final String LEVEL_KEY = "level";

    /**
     * Add visual params to display indoor-map
     *
     * @param properties
     * @param entity
     * @return
     */
    private Map<String, Object> decorateBackground(Map<String, Object> properties, AssetContainerEntity entity) {

        properties.put(STROKE_COLOR_KEY, STROKE_COLOR);
        properties.put(STROKE_WIDTH_KEY, STROKE_WIDTH);
        properties.put(STROKE_OPACITY_KEY, STROKE_OPACITY);
        properties.put(FILL_KEY, FILL_COLOR);
        properties.put(FILL_OPACITY_KEY, FILL_OPACITY);

        properties.put(LEVEL_KEY, 0);

        return properties;
    }

    private static final Logger LOG = Logger.getLogger(SpatialAssetController.class.getName());

    @Autowired
    private AssetStore assetStore;

    @Autowired
    private ModelStore modelStore;

    @Autowired
    private SpatialVisitor spatialVisitor;

    @Autowired
    private DetailsVisitor detailsVisitor;

    private FeatureCollection markers(FeatureCollection dto, UUID assetId) {
        List<Feature> features = new ArrayList<>();
        
        assetStore.getAssetTreeDao().findLeafChildren(assetId).forEach(asset -> {
            features.add(new DataExtractorDto(
                    asset.getId(), 
                    asset.getMetaEntity().getId(),
                    asset.getLocation().getY(), 
                    asset.getLocation().getX(), 
                    0.0
            ));
        });
        
        if (dto.getFeatures() == null) dto.setFeatures(new ArrayList<>());
        dto.getFeatures().addAll(features);
        
        return dto;
    }
    
    private FeatureCollection background(FeatureCollection dto, UUID assetId) {
        List<Feature> features = new ArrayList<>();

        // Add all background GeoJSON feature to one collection.
        assetStore.getAssetTreeDao().findForkChildren(assetId).forEach(asset -> {
            asset.getGeoJson().forEach(a -> features.add(a));
        });

        if (dto.getFeatures() == null) dto.setFeatures(new ArrayList<>());
        dto.getFeatures().addAll(features);

        return dto;
    }

    @ApiOperation(value = "/roots", notes = "Get root sites from the asset-tree.")
    @RequestMapping(value = "/roots", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public InfraDto roots() {

        LOG.log(Level.INFO, "Get root sites from the asset-tree.");
        InfraDto dto = new InfraDto();
        this.assetStore.getAssetTreeDao().findRoots().forEach(entity -> {
            dto.getFeatures().add(spatialVisitor.visit(entity));
        });
        return dto;
    }

    @ApiOperation(value = "/site/{assetId}", notes = "Get children of an asset in the asset-tree.")
    @RequestMapping(value = "/site/{assetId}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public InfraDto children(@PathVariable("assetId") String assetId) {
        LOG.log(Level.INFO, "Get children of asset[{0}] in the asset-tree.", assetId);
        InfraDto dto = new InfraDto();
        this.assetStore.getAssetTreeDao().findLeafChildren(UUID.fromString(assetId)).forEach(entity -> {
            dto.getFeatures().add(spatialVisitor.visit(entity));
        });
        this.assetStore.getAssetTreeDao().findForkChildren(UUID.fromString(assetId)).forEach(entity -> {
            dto.getFeatures().add(spatialVisitor.visit(entity));
        });

        return dto;
    }

    @ApiOperation(value = "/location/{assetId}", notes = "Get details of a location by asset.")
    @RequestMapping(value = "/location/{assetId}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public DataExtractorDto details(@PathVariable("assetId") String assetId) {
        LOG.log(Level.INFO, "Get location details of asset[{0}].", assetId);
        return detailsVisitor.visitBase(assetStore.getAssetTreeDao().findOne(UUID.fromString(assetId)));
    }

    @ApiOperation(value = "/properties/{assetId}", notes = "Get properties of a location by asset.")
    @RequestMapping(value = "/properties/{assetId}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<MetaAttributeDto> metrics(@PathVariable("assetId") String assetId) {

        List<MetaAttributeDto> metrics = new ArrayList<>();
        final AssetEntity assetEntity = assetStore.getAssetTreeDao().findOne(UUID.fromString(assetId));

        LOG.log(Level.INFO, "Get location properties of asset[{0}] (classification: {1}).", new Object[]{assetId, assetEntity.getMetaEntity().getId()});

        modelStore.getMetaEntityDao().findAttributesOf(assetEntity.getMetaEntity().getId())
                .forEach(attribute -> {
                    final MetaAttributeDto dto = attribute.getMetric().getDto();
                    dto.setId(attribute.getId());
                    dto.setName(attribute.getMd().getLabel());
                    dto.setDescription(attribute.getMd().getDescription());
                    metrics.add(dto);
                });
        return metrics;
    }
}
