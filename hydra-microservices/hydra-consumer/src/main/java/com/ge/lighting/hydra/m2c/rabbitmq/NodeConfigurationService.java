/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.m2c.rabbitmq;

import com.ge.lighting.hydra.core.entity.AssetContainerEntity;
import com.ge.lighting.hydra.core.entity.AssetEntity;
import com.ge.lighting.hydra.core.entity.AssetExtractorEntity;
import com.ge.lighting.hydra.core.repository.AssetTreeRepository;
import com.ge.lighting.hydra.m2c.asset.Machine;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Node configuration service.
 * @author predix
 */
@Service
public class NodeConfigurationService {

    private static final Logger LOG = Logger.getLogger(NodeConfigurationService.class.getName());

    @Autowired
    private AssetTreeRepository assetTreeDao;

    private AssetEntity findOrCreate(UUID id) {
        AssetEntity node = assetTreeDao.findOne(id);
        if (node == null) {
            node = new AssetExtractorEntity();
            node.setId(id);
            assetTreeDao.save(node);
            LOG.log(Level.INFO, "Create machine {0}", id);
        }
        return node;
    }

    @RabbitListener(queues = {"connect"})
    public void startUp(Machine configuration) {
        AssetEntity node = this.findOrCreate(configuration.getId());
        node.setActive(true);
        assetTreeDao.save(node);
        LOG.log(Level.INFO, "Startup machine {0}", configuration.getId());
    }

    @RabbitListener(queues = {"disconnect"})
    public void shutDown(Machine configuration) {
        AssetEntity node = this.findOrCreate(configuration.getId());
        node.setActive(false);
        assetTreeDao.save(node);
        LOG.log(Level.INFO, "Shutdown machine {0}", configuration.getId());
    }
}
