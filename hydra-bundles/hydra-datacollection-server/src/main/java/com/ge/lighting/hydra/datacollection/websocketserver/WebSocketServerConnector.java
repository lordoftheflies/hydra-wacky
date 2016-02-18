/*
 * Copyright (c) 2014 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.ge.lighting.hydra.datacollection.websocketserver;

import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

import com.ge.lighting.hydra.datacollection.DataIngestionService;

/**
 *
 * @author Predix Machine Sample
 */
@Component(
        provide = WebSocketServerConnector.class,
        name = WebSocketServerConnector.SERVICE_PID
)
public class WebSocketServerConnector {

    /**
     * SERVICE_PID String used as the component service name
     */
    protected final static String SERVICE_PID = "com.ge.lighting.emea.innovation.datacollection.ws";
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServerConnector.class);
    private ServerContainer serverContainer;

    private DataIngestionService dataIngestionService;

    @Reference
    public void setDataIngestionService(DataIngestionService dataIngestionService) {
        this.dataIngestionService = dataIngestionService;
    }

    public void unsetDataIngestionService(DataIngestionService dataIngestionService) {
        dataIngestionService = null;
    }

    /**
     * Adds a server endpoint to the server container
     *
     * @throws DeploymentException Depoloyment exception thrown if server
     * deployment is unsuccessful
     */
    @Activate
    public void activate(ComponentContext ctx) {
        try {
            this.serverContainer.addEndpoint(RealTimeWebSocketServerMsgHandler.class);
            LOG.info("Hydra Data-collection service Websocket: Server endpoint for key-value stream deployed");    //endpoint is immediately available //$NON-NLS-1$
            this.serverContainer.addEndpoint(MessageWebSocketServerMsgHandler.class);
            LOG.info("Hydra Data-collection service Websocket: Server endpoint for messages deployed");    //endpoint is immediately available //$NON-NLS-1$
        } catch (DeploymentException e) {
            LOG.error("Hydra Data-collection service Websocket: Error occurred while deploying the server endpoint", e); //$NON-NLS-1$
        }
    }

    @Deactivate
    public void deactivate(ComponentContext ctx) {
        LOG.info("Hydra Data-collection service Websocket: Server endpoint for key-value stream is disposing");    //endpoint is immediately available //$NON-NLS-1$
        LOG.info("Hydra Data-collection service Websocket: Server endpoint for messages is disposing");    //endpoint is immediately available //$NON-NLS-1$
    }

    /**
     * Dependency injection of server container
     *
     * @param serverContainer The server container
     */
    @Reference
    protected void setServerContainerContainer(ServerContainer serverContainer) {
        this.serverContainer = serverContainer;
    }

    /**
     * Unsets the server container
     *
     * @param serverContainer The server container
     */
    protected void unsetServerContainerContainer(@SuppressWarnings("hiding") ServerContainer serverContainer) {
        this.serverContainer = null;
    }
}
