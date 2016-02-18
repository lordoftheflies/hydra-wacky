/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.mq.connector.rabbitmq;

import com.ge.lighting.hydra.asset.AssetStore;
import com.ge.lighting.hydra.asset.device.entity.AssetEntity;
import com.ge.lighting.hydra.asset.device.entity.AssetExtractorEntity;
import com.ge.lighting.hydra.asset.device.repository.AssetTreeRepository;
import com.ge.lighting.hydra.m2m.common.Machine;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Node configuration service.
 * @author predix
 */
@Component
public class NodeConfigurationService {

    private static final Logger LOG = Logger.getLogger(NodeConfigurationService.class.getName());

//    @Autowired
    private AssetStore assetStore;
    
    private AssetEntity findOrCreate(UUID id) {
        AssetEntity node = assetStore.getAssetTreeDao().findOne(id);
        if (node == null) {
            node = new AssetExtractorEntity();
            node.setId(id);
            assetStore.getAssetTreeDao().save(node);
            LOG.log(Level.INFO, "Create machine {0}", id);
        }
        return node;
    }

//    @RabbitListener(queues = {"connect"})
    public void startUp(Machine configuration) {
        AssetEntity node = this.findOrCreate(configuration.getId());
        node.setActive(true);
        assetStore.getAssetTreeDao().save(node);
        LOG.log(Level.INFO, "Startup machine {0}", configuration.getId());
    }

//    @RabbitListener(queues = {"disconnect"})
    public void shutDown(Machine configuration) {
        AssetEntity node = this.findOrCreate(configuration.getId());
        node.setActive(false);
        assetStore.getAssetTreeDao().save(node);
        LOG.log(Level.INFO, "Shutdown machine {0}", configuration.getId());
    }
}
