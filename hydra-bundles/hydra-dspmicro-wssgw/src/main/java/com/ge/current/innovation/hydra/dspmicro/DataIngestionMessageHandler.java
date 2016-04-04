/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.hydra.dspmicro;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import com.ge.current.innovation.DataPoint;
import com.ge.current.innovation.hydra.dspmicro.gw.WebSocketClientContainer;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
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
 *
 * @author lordoftheflies
 */
@ServerEndpoint(value = "/data/{nodeId}/ingest")
//@Component
public class DataIngestionMessageHandler {
    
    private static Logger _logger = LoggerFactory.getLogger(DataIngestionMessageHandler.class);
    
    private WebSocketClientContainer clientContainer;
    
    public DataIngestionMessageHandler() {
        BundleContext context = FrameworkUtil.getBundle(DataIngestionMessageHandler.class).getBundleContext();
        _logger.info("Itt a bundle CONTEXT: " + context);
        ServiceReference<WebSocketClientContainer> serviceReference = context.getServiceReference(WebSocketClientContainer.class);
        _logger.info("Itt a SERVICE-REFERENCE: " + serviceReference);
        _logger.info("Itt a SERVICE-REFERENCE: " + context.getServiceReference(WebSocketServerConnector.class));
        clientContainer = context.getService(serviceReference);
        
    }

    /**
     *
     * @param session The web socket session
     */
    @OnOpen
    public void onOpen(@PathParam("nodeId") String guestID, Session session) {
        _logger.info("Field-agent opened a gateway websocket for " + guestID + " in session " + session.getId()); //$NON-NLS-1$
    }

    /**
     * Defines the behavior of the server when a string message is received.
     * Receives the message from the client, prints to the logger, and sends
     * back.
     *
     * @param message The string message that was received
     * @param session The web socket session
     * @return An echo string message that is sent to the client
     */
    @OnMessage
    public String onStringMessage(@PathParam("nodeId") String guestID, String message, Session session) {
        _logger.info("Field-agent gateway websocket server received from " + guestID + ": " + message + "."); //$NON-NLS-1$ //$NON-NLS-2$
        this.clientContainer.send(new DataPoint());
        return "INGESTED";
    }

//    /**
//     * Defines the behavior of the server when a ByteBuffer message is received.
//     * Receives the message from the client, prints to the logger, and sends
//     * back.
//     *
//     * @param message The ByteBuffer message that was received
//     * @param session The web socket session
//     * @return An echo ByteBuffer message that is sent to the client
//     */
//    @OnMessage
//    public ByteBuffer onByteMessage(@PathParam("nodeId") String guestID, ByteBuffer message, Session session) {
//        String result = new String(message.array(), Charset.forName("UTF-8")); //$NON-NLS-1$
//        _logger.info("Field-agent gateway websocket server received from " + guestID + ": " + result + "."); //$NON-NLS-1$ //$NON-NLS-2$
//        return message;
//    }
    /**
     * Defines the behavior of the server when the session is closed. Prints to
     * the logger.
     *
     * @param session The web socket session
     * @param closeReason Provides information on the session close including
     * close reason phrase, code, etc...
     */
    @OnClose
    public void onClose(@PathParam("nodeId") String guestID, Session session, CloseReason closeReason) {
        _logger.info("Field-agent gateway websocket for " + guestID + " in session " + session.getId() + " closed because of " + closeReason.toString()); //$NON-NLS-1$ //$NON-NLS-2$
    }

//    /**
//     * Dependency injection of server container
//     *
//     * @param serverContainer The server container
//     */
//    @Reference
//    public void setClientContainer(WebSocketClientContainer clientContainer) {
//        this.clientContainer = clientContainer;
//    }
//
//    /**
//     * Unsets the server container
//     *
//     * @param serverContainer The server container
//     */
//    public void unsetClientContainer(@SuppressWarnings("hiding") WebSocketClientContainer clientContainer) {
//        this.clientContainer = null;
//    }
}
