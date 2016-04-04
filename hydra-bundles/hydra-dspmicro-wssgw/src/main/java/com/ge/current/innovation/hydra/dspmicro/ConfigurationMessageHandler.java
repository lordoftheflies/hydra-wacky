/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.hydra.dspmicro;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lordoftheflies
 */
@ServerEndpoint(value = "/configure/{nodeId}")
public class ConfigurationMessageHandler
{
    private static Logger _logger = LoggerFactory.getLogger(ConfigurationMessageHandler.class);

    /**
     * 
     * @param session The web socket session
     */
    @OnOpen
    public void onOpen(@PathParam("nodeId") String guestID, Session session)
    {
        _logger.info("Server: opened... " + session.getId()); //$NON-NLS-1$
    }

    /**
     * Defines the behavior of the server when a string message is received.
     * Receives the message from the client, prints to the logger, and sends back.
     * 
     * @param message The string message that was received
     * @param session The web socket session
     * @return An echo string message that is sent to the client
     */
    @OnMessage
    public String onStringMessage(@PathParam("nodeId") String guestID, String message, Session session)
    {
        _logger.info("Server: received... " + message + ". Sending back"); //$NON-NLS-1$ //$NON-NLS-2$
        return message;
    }

    /**
     * Defines the behavior of the server when a ByteBuffer message is received.
     * Receives the message from the client, prints to the logger, and sends back.
     * 
     * @param message The ByteBuffer message that was received
     * @param session The web socket session
     * @return An echo ByteBuffer message that is sent to the client
     */
    @OnMessage
    public ByteBuffer onByteMessage(@PathParam("nodeId") String guestID, ByteBuffer message, Session session)
    {
        String result = new String(message.array(), Charset.forName("UTF-8")); //$NON-NLS-1$
        _logger.info("Server: received... " + result + ". Sending back"); //$NON-NLS-1$ //$NON-NLS-2$
        return message;
    }

    /**
     * Defines the behavior of the server when the session is closed.
     * Prints to the logger.
     * 
     * @param session The web socket session
     * @param closeReason Provides information on the session close including
     *            close reason phrase, code, etc...
     */
    @OnClose
    public void onClose(@PathParam("nodeId") String guestID, Session session, CloseReason closeReason)
    {
        _logger.info("Server: Session " + session.getId() + " closed because of " + closeReason.toString()); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
