/*
 * Copyright (c) 2014 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.ge.lighting.hydra.dataextraction.websocketserver;

import com.ge.lighting.hydra.m2m.common.AssetMonitoringException;
import com.ge.lighting.hydra.m2m.common.DataPoint;
import com.ge.lighting.hydra.m2m.common.Edge;
import com.ge.lighting.hydra.m2m.common.Machine;
import com.ge.lighting.hydra.m2m.common.Sensor;
import com.ge.lighting.hydra.m2m.json.AssetCoder;
import java.nio.ByteBuffer;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles messages received by the client. Messages can be sent from
 * this class or from the container class by using the websocket session object.
 *
 * @author Predix Machine Sample
 */
@ClientEndpoint(
        decoders = {AssetCoder.SensorDecoder.class, AssetCoder.EdgeDecoder.class, AssetCoder.MachineDecoder.class},
        encoders = {AssetCoder.SensorEncoder.class, AssetCoder.EdgeEncoder.class, AssetCoder.MachineEncoder.class}
)
public class AssetMonitorWebSocketClientHandler {

    private static Logger _logger = LoggerFactory.getLogger(WsClientBatchMsgHandler.class.getName());

    /**
     * Defines the behavior of the handler when a session is opened. Prints the
     * status to the logger.
     *
     * @param session The web socket session
     */
    @OnOpen
    public void onOpen(Session session) {
        _logger.info("Hydra Asset-mock service open " + session.getId()); //$NON-NLS-1$
    }
    
    @OnError
    public void onError(Throwable t) {
        _logger.error("Hydra Asset-mock service error: " + t.getMessage() + ".");
    }
    
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        _logger.info("Hydra Asset-mock service session " + session.getId() + " closed because of " + closeReason.toString()); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @OnMessage
    public void onMessage(Sensor sensor, Session session) throws AssetMonitoringException {
        _logger.info("Hydra Asset-mock service receive SENSOR configuration update: " + sensor);
    }
    
    @OnMessage
    public void onMessage(Edge edge, Session session) throws AssetMonitoringException {
        _logger.info("Hydra Asset-mock service receive EDGE configuration update: " + edge);
    }
    
    @OnMessage
    public void onMessage(Machine machine, Session session) throws AssetMonitoringException {
        _logger.info("Hydra Asset-mock service receive MACHINE configuration update: " + machine);
    }
}
