/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.asset;

import com.ge.lighting.hydra.asset.device.repository.AssetTreeRepository;
import com.ge.lighting.hydra.asset.model.repository.MetaEntityRepository;
import com.ge.lighting.hydra.asset.model.repository.MetricEntityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

/**
 *
 * @author Hegedűs László (212429780)
 */
@Component
@EnableAutoConfiguration
public class ModelStore {
    
    static final String ENTITY_PACKAGE = "com.ge.lighting.hydra.asset.model.entity";
    static final String DAL_PACKAGE = "com.ge.lighting.hydra.asset.model.repository";
    
    @Autowired
    private MetricEntityRepository metricDao;

    public MetricEntityRepository getMetricDao() {
        return metricDao;
    }

    public void setMetricDao(MetricEntityRepository metricDao) {
        this.metricDao = metricDao;
    }
    
    @Autowired
    private MetaEntityRepository metaEntityDao;

    public MetaEntityRepository getMetaEntityDao() {
        return metaEntityDao;
    }

    public void setMetaEntityDao(MetaEntityRepository metaEntityDao) {
        this.metaEntityDao = metaEntityDao;
    }
}
