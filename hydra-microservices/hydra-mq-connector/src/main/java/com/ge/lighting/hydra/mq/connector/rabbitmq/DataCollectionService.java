/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.mq.connector.rabbitmq;

import com.ge.lighting.hydra.datastore.DataStore;
import com.ge.lighting.hydra.datastore.entity.DataKey;
import com.ge.lighting.hydra.datastore.entity.DataPointEntity;
import com.ge.lighting.hydra.m2m.common.DataPoint;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author predix
 */
@Component
public class DataCollectionService {
    private static final Logger LOG = Logger.getLogger(DataCollectionService.class.getName());
  
    @Autowired
    private DataStore dataStore;
    
    @RabbitListener(queues = {"data"})
    public void processDev(DataPoint data) {
        LOG.log(Level.INFO, "Received: {0}, latency: {1} ", new Object[]{
            data.toString(), 
            (new Date().getTime() -data.getTs().getTime())/6000
        });
        dataStore.getDataStoreDao().save(new DataPointEntity(new DataKey(data.getKey(), data.getTs(), data.getAssetId()), data.getValue()));
    }
    
    
}
