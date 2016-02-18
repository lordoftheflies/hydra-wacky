/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.asset;

import com.ge.lighting.hydra.asset.device.repository.AssetTreeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

/**
 *
 * @author Hegedűs László (212429780)
 */
@Component
@EnableAutoConfiguration
public class AssetStore {

    static final String ENTITY_PACKAGE = "com.ge.lighting.hydra.asset.device.entity";
    static final String DAL_PACKAGE = "com.ge.lighting.hydra.asset.device.repository";

    @Autowired
    private AssetTreeRepository assetTreeDao;

    public AssetTreeRepository getAssetTreeDao() {
        return assetTreeDao;
    }

    public void setAssetTreeDao(AssetTreeRepository assetTreeDao) {
        this.assetTreeDao = assetTreeDao;
    }

}
