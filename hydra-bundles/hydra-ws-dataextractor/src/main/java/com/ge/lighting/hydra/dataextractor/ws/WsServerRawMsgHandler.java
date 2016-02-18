/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.dataextractor.ws;

import com.ge.lighting.hydra.datacollection.DataIngestionService;
import com.ge.lighting.hydra.m2m.common.DataPoint;
import com.ge.lighting.hydra.m2m.common.DataUpstreamException;
import com.ge.lighting.hydra.m2m.json.DataCoder;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint(	
        value = "/data/raw/"
)
public class WsServerRawMsgHandler {
    private static Logger LOG = LoggerFactory.getLogger(WsServerBatchMsgHandler.class);

//    private DataIngestionService dataIngestionService;

    public WsServerRawMsgHandler() {
//        BundleContext context = FrameworkUtil.getBundle(WsServerBatchMsgHandler.class).getBundleContext();
//        ServiceReference<DataIngestionService> serviceReference = context.getServiceReference(DataIngestionService.class);
//        dataIngestionService = context.getService(serviceReference);
    }

    /**
     *
     * @param session The web socket session
     */
    @OnOpen
    public void onOpen(Session session) {
        LOG.info("Hydra Data-collection service open " + session.getId()); //$NON-NLS-1$
    }

    /**
     * Defines the behavior of the server when a string message is received.
     * Receives the message from the client, prints to the logger, and sends
     * back.
     *
     * @param message The string message that was received
     * @param session The web socket session
     * @throws com.ge.lighting.hydra.m2m.common.DataUpstreamException
     */
    @OnMessage
    public void onMessage(String message, Session session) throws DataUpstreamException {
        LOG.info("Hydra Data-collection service receive text message: " + message);
    }
    

    @OnError
    public void onError(Throwable t) {
        LOG.error("Hydra Data-collection service error: " + t.getMessage() + ".");
        t.printStackTrace();
    }
    /**
     * Defines the behavior of the server when the session is closed. Prints to
     * the logger.
     *
     * @param session The web socket session
     * @param closeReason Provides information on the session close including
     * close reason phrase, code, etc...
     */
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        LOG.info("Hydra Data-collection service session " + session.getId() + " closed because of " + closeReason.toString()); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
