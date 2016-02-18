/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.infrastructure.rest;

import com.ge.lighting.hydra.asset.AssetStore;
import com.ge.lighting.hydra.asset.ModelStore;
import com.ge.lighting.hydra.asset.device.entity.AssetEntity;
import com.ge.lighting.hydra.infrastructure.dto.AssetDetailsDto;
import com.ge.lighting.hydra.infrastructure.dto.AssetNodeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author predix
 */
@Api(value = "AssetTreeController")
@RestController
@RequestMapping("/assettree")
public class AssetTreeController {

    @Autowired
    private AssetStore assetDao;
    @Autowired
    private ModelStore modelStore;

    @RequestMapping(
            value = "/roots",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/roots", nickname = "Root assets.", notes = "Root assets.")
    public List<AssetNodeDto> roots() {
        List<AssetNodeDto> dto = new ArrayList<>();
        assetDao.getAssetTreeDao().findRoots().forEach(a
                -> dto.add(new AssetNodeDto(a.getId(),
                        null,
                        a.getMetaEntity().getMd().getState(),
                        a.getMetaEntity().getId().toString(),
                        a.getFriendlyName(),
                        a.getDescription()
                )));
        return dto;
    }

    @RequestMapping(
            value = "/children/{id}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/children/{id}", nickname = "Children assets.", notes = "Children assets of the parent..")
    public List<AssetNodeDto> children(
            @PathVariable("id") String id
    ) {
        List<AssetNodeDto> dto = new ArrayList<>();
        final UUID parentId = UUID.fromString(id);
        assetDao.getAssetTreeDao().findChildren(parentId).forEach(a
                -> dto.add(new AssetNodeDto(a.getId(),
                        parentId,
                        a.getMetaEntity().getMd().getState(),
                        a.getMetaEntity().getId().toString(),
                        a.getFriendlyName(),
                        a.getDescription()
                )));
        return dto;
    }

    @RequestMapping(value = "/details/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.GET)
    @ApiOperation(value = "/details/{id}", nickname = "Asset details.", notes = "Details of the asset.")
    public AssetDetailsDto details(@PathVariable("id") String id) {
        AssetEntity entity = assetDao.getAssetTreeDao().findOne(UUID.fromString(id));
        Map<String, Object> props = new HashMap<>();
        modelStore.getMetaEntityDao().findAttributesOf(entity.getMetaEntity().getId()).forEach(a
                -> props.put(a.getMetric().getId(), null));
        return new AssetDetailsDto(entity.getId(),
                (entity.getParent() == null) ? null : entity.getParent().getId(),
                entity.getMetaEntity().getMd().getState(),
                entity.getMetaEntity().getId().toString(),
                entity.getFriendlyName(),
                entity.getDescription(),
                props
        );
    }
}
