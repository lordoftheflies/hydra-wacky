/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.dataingestion.consumer.boot;

import static com.ge.current.innovation.dataingestion.consumer.boot.RabbitLocalConfig.EXCHANGE_INGESTION;
import com.ge.current.innovation.storage.jpa.dal.AssetRepository;
import com.ge.current.innovation.storage.jpa.entities.AssetEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author lordoftheflies
 */
@Component
public class ConnectionManagerService {

    private static final Logger LOG = Logger.getLogger(ConnectionManagerService.class.getName());

    @Autowired
    private AssetRepository assetMeterRepository;

    @Autowired
    private RabbitAdmin rabbitAdmin;
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TopicExchange exchange;

    @Qualifier("outputQueue")
    @Autowired
    private Queue outputQueue;

    @Autowired
    @Qualifier("gw")
    private SimpleMessageListenerContainer container;

    public List<String> connectedAssets() {
        return assetMeterRepository.findAll().stream()
                .map((AssetEntity a) -> a.getNodeId())
                .filter((String nodeId) -> nodeId != null)
                .collect(Collectors.toList());
    }

    @PostConstruct
    public void initialize() {
        LOG.log(Level.INFO, "Initialize MQ ...");
        List<Queue> queues = new ArrayList<>();
        this.connectedAssets().forEach(nodeId -> {
            LOG.log(Level.INFO, "\t- Declare queue {0}", nodeId);
            Queue nodeQueue = new Queue(nodeId);
            rabbitAdmin.declareQueue(nodeQueue);
//            rabbitAdmin.declareBinding(BindingBuilder.bind(nodeQueue).to(exchange).with(EXCHANGE_INGESTION));
            queues.add(nodeQueue);
        });
        

        LOG.log(Level.INFO, "\t- Declare queue stdout.");
        Queue[] qA = new Queue[queues.size() + 1];
        qA = queues.toArray(qA);
        qA[queues.size()] = outputQueue;
        container.setQueues(qA);
        
        this.rabbitAdmin.initialize();
    }
}
