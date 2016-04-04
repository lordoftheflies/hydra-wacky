/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.hydra.dspmicro;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import java.security.Policy;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lordoftheflies
 */
@Component(name = WebSocketServerConnector.SERVICE_PID)
public class WebSocketServerConnector {

    /**
     * SERVICE_PID String used as the component service name
     */
    protected final static String SERVICE_PID = "com.ge.current.innovation.hydra.dspmicro.wsserver";                            //$NON-NLS-1$
    private static Logger _logger = LoggerFactory.getLogger(WebSocketServerConnector.class);
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(WebSocketServerConnector.class.getName());
    
    private ServerContainer serverContainer;

    /**
     * Adds a server endpoint to the server container
     *
     * @throws DeploymentException Depoloyment exception thrown if server
     * deployment is unsuccessful
     */
    @Activate
    public void addServer()
            throws DeploymentException {
        try {
            
            this.serverContainer.addEndpoint(DataIngestionMessageHandler.class);
            this.serverContainer.addEndpoint(ConfigurationMessageHandler.class);
            LOG.info("FFFFFFFFFFFFFFFFFFFFOS");
            _logger.info("Server: Server endpoint deployed");    //endpoint is immediately available //$NON-NLS-1$
        } catch (DeploymentException e) {
            _logger.error("Server: Error occurred while deploying the server endpoint", e); //$NON-NLS-1$
        }
        
        Policy.setPolicy(new EdgeDriverPolicy());
    }
    
    @Deactivate
    public void clearServer() {
        LOG.info("Deactivate websocket gateway.");
        _logger.info("Deactivate websocket gateway.");    //endpoint is immediately available //$NON-NLS-1$
    }

    /**
     * Dependency injection of server container
     *
     * @param serverContainer The server container
     */
    @Reference
    public void setServerContainerContainer(ServerContainer serverContainer) {
        this.serverContainer = serverContainer;
    }

    /**
     * Unsets the server container
     *
     * @param serverContainer The server container
     */
    public void unsetServerContainerContainer(@SuppressWarnings("hiding") ServerContainer serverContainer) {
        this.serverContainer = null;
    }
    
    
    
}
