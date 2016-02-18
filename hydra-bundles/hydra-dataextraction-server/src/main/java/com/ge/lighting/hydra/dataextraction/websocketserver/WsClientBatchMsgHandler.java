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

import com.ge.lighting.hydra.m2m.common.DataPoint;
import com.ge.lighting.hydra.m2m.json.DataCoder.DataPointDecoder;
import com.ge.lighting.hydra.m2m.json.DataCoder.DataPointEncoder;
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
        decoders = {DataPointDecoder.class},
        encoders = {DataPointEncoder.class}
)
public class WsClientBatchMsgHandler {

    private static Logger _logger = LoggerFactory.getLogger(WsClientBatchMsgHandler.class.getName());

    /**
     * Defines the behavior of the handler when a session is opened. Prints the
     * status to the logger.
     *
     * @param session The web socket session
     */
    @OnOpen
    public void onOpen(Session session) {
        _logger.info("Hydra Data-extraction service open " + session.getId()); //$NON-NLS-1$
    }
    
    @OnError
    public void onError(Throwable t) {
        _logger.error("Hydra Data-extraction service error: " + t.getMessage() + ".");
    }
    
    /**
     * Defines the behavior of the handler when a byte message is received.
     * Checks the message for validity and logs transmission success or failure
     *
     * @param message The byte buffer message that was received
     * @param session The web socket session
     */
    @OnMessage
    public void onMessage(DataPoint message, Session session) {
        _logger.info("Hydra Data-extraction service received DATAPOINT message:\n\t" + message.toString()); //$NON-NLS-1$ //$NON-NLS-2$
    
    }

    /**
     * Defines the behavior of the client message handler when the session is
     * closed.
     *
     * @param session The web socket session
     * @param closeReason Provides information on the session close including
     * close reason phrase, code, etc...
     */
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        _logger.info("Hydra Data-extraction service session " + session.getId() + " closed because of " + closeReason.toString()); //$NON-NLS-1$ //$NON-NLS-2$
    }

}
