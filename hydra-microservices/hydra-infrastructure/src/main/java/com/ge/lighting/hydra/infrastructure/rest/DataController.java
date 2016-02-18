/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.infrastructure.rest;

import com.ge.lighting.hydra.asset.AssetStore;
import com.ge.lighting.hydra.asset.ModelStore;
import com.ge.lighting.hydra.datastore.DataStore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author predix
 */
@RestController
@Api(value = "Data controller")
@RequestMapping("/data")
public class DataController {
    
    @Autowired
    private DataStore dataStore;
    @Autowired
    private AssetStore assetStore;
    @Autowired
    private ModelStore modelStore;
    
    
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET, value = "/{id}/values")
    @ApiOperation(value = "/{id}/values", notes = "Current sensor values.")
    @ResponseBody
    public Map<String, Object> currentValues(@PathVariable("id") String assetId) {
        Map<String, Object> result = new HashMap<>();
        List<String> keys = new ArrayList<>();
        final UUID assetUid = UUID.fromString(assetId);
        UUID classificationId = assetStore.getAssetTreeDao().findOne(assetUid).getMetaEntity().getId();
        modelStore.getMetaEntityDao().findAttributesOf(classificationId).forEach(a -> {
            keys.add(a.getMetric().getId());
            result.put(a.getMetric().getId(), 0);
                });
        dataStore.getDataStoreDao().findLastValues(assetUid, keys).forEach(a ->
        result.put(a.getId().getxType(), a.getVal()));
        return result;
    }
}
