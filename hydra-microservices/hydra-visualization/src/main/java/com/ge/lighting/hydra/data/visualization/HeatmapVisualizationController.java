/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.data.visualization;

import com.ge.lighting.hydra.asset.AssetStore;
import com.ge.lighting.hydra.asset.device.entity.AssetExtractorEntity;
import com.ge.lighting.hydra.datastore.DataStore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author predix
 */
@Api(value = "Heatmap visualization")
@RestController
@RequestMapping("/spatial")
public class HeatmapVisualizationController {

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
    
    private static final Logger LOG = Logger.getLogger(HeatmapVisualizationController.class.getName());

    @Autowired
    private DataStore dataStore;

    @Autowired
    private AssetStore assetStore;

    @RequestMapping(
            value = "/realtime",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/realtime", nickname = "Current values", notes = "Status of the area. This shows the last arrived sensor data.")
    public HeatMapDto realtime() {
        Random r = new Random();
        HeatMapDto dto = new HeatMapDto(
                new HeatMapDataPoint(19.081575572490692, 47.57675008274512, r.nextDouble()),
                new HeatMapDataPoint(19.081637263298035, 47.57675189216864, r.nextDouble()),
                new HeatMapDataPoint(19.081572890281674, 47.57672113196058, r.nextDouble())
        );
        Map<UUID, AssetExtractorEntity> result = new HashMap<>();
        this.assetStore.getAssetTreeDao().findLeafs().forEach(a -> {
            result.put(a.getId(), a);
        });
        
        this.dataStore.getDataStoreDao().findAll().forEach(a -> {
            dto.add(new HeatMapDataPoint(result.get(a.getId().getAsset()).getLocation().getY(), result.get(a.getId().getAsset()).getLocation().getX(), a.getVal() * 1000));
        });
        
        return dto;
    }

    @RequestMapping(
            value = "/{id}/{key}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/{id}/{key}", nickname = "Current values", notes = "Status of the area. This shows the last arrived sensor data.")
    public HeatMapDto heatmap(
            @PathVariable("id") String id,
            @PathParam("key") String key,
            @RequestParam(value = "begin", required = true) String begin,
            @RequestParam(value = "end", required = true) String end,
            @RequestParam(value = "limit", defaultValue = "100") int limit,
            @RequestParam(value = "pages", defaultValue = "0") int page) throws ParseException {
        LOG.info("Rendering heatmap layer ...");

        Date start = df.parse(begin);
        Date stop = df.parse(end);
        
        HeatMapDto dto = new HeatMapDto();
        assetStore.getAssetTreeDao().findLeafChildren(UUID.fromString(id)).forEach(extractor -> {
            dto.add(new HeatMapDataPoint(
                    extractor.getLocation().getX(), 
                    extractor.getLocation().getY(), 
                    dataStore.getDataStoreDao().avgOfVolume(
                            extractor.getId(), 
                            key, 
                            start,
                            stop)));
        });

        return dto;
    }

}
