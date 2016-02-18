/*
 * Copyright (c) 2014 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.ge.lighting.hydra.dataextractor.ws;

import com.ge.lighting.hydra.datacollection.DataIngestionService;
import com.ge.lighting.hydra.m2m.common.DataPoint;
import com.ge.lighting.hydra.m2m.common.DataUpstreamException;
import com.ge.lighting.hydra.m2m.json.DataCoder;
import java.util.Date;
import java.util.UUID;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data-collection websocket server
 *
 * @author Hegedűs László (212429780)
 */
@ServerEndpoint(
        value = "/data/{asset}/realtime"
)
public class WsServerRealTimeMsgHandler {

    private static Logger LOG = LoggerFactory.getLogger(WsServerRealTimeMsgHandler.class);
    private static final String KEY_VALUE_DELIMITER = "=";
    private static final String RESULT_SUCCESS = "success";
    private static final String RESULT_FAIL = "fail";

    private DataIngestionService dataIngestionService;

    public WsServerRealTimeMsgHandler() {
        BundleContext context = FrameworkUtil.getBundle(WsServerRealTimeMsgHandler.class).getBundleContext();
        ServiceReference<DataIngestionService> serviceReference = context.getServiceReference(DataIngestionService.class);
        dataIngestionService = context.getService(serviceReference);
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
     * @param assetId
     * @param message The string message that was received
     * @param session The web socket session
     * @throws com.ge.lighting.hydra.m2m.common.DataUpstreamException
     */
    @OnMessage
    public void onMessage(@PathParam("asset") String assetId, String message, Session session) throws DataUpstreamException {
        LOG.info("Hydra Data-collection service receive RAW message: " + message);
        String[] tokens = message.split(KEY_VALUE_DELIMITER);
        this.dataIngestionService.send(new DataPoint(
                tokens[0], 
                new Date(), 
                UUID.fromString(assetId), 
                Double.parseDouble(tokens[1])));
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
