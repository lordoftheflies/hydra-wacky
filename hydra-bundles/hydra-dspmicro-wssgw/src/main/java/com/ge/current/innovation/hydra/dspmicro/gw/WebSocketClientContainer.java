/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.hydra.dspmicro.gw;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import com.ge.current.innovation.DataPoint;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lordoftheflies
 */
@Component(name = WebSocketClientContainer.SERVICE_PID)
public class WebSocketClientContainer {

    /**
     * Create logger to report errors, warning massages, and info messages
     * (runtime Statistics)
     */
    protected static Logger _logger = LoggerFactory.getLogger(WebSocketClientContainer.class);

    /**
     * SERVICE_PID String used as the component service name
     */
    protected final static String SERVICE_PID = "com.ge.current.innovation.hydra.dspmicro.wsclient";                            //$NON-NLS-1$

//    private static final String LOCAL_WSSERVER = "ws://localhost:5457/testwebsocketendpoint";                  //$NON-NLS-1$
    private static final String LOCAL_WSSERVER = "ws://hydra-dataingestion-amqp-producer-wss.run.aws-usw02-pr.ice.predix.io/data";                  //$NON-NLS-1$

    /**
     * TEST_STRING A test string to be used in validating correct websocket
     * transfer
     */
    static final String TEST_STRING = "A test string";                                              //$NON-NLS-1$
    /**
     * TEST_BYTE A test string that will be converted to a byte buffer to be
     * used in validating correct websocket transfer
     */
    static final String TEST_BYTE = "A test of binary data sending";                              //$NON-NLS-1$
    /**
     * TEST_BYTE_BUFF1 A test byte buffer to be used in validating correct
     * websocket transfer
     */
    static final ByteBuffer TEST_BYTE_BUFF = ByteBuffer.wrap(TEST_BYTE.getBytes(Charset.forName("UTF-8")));       //$NON-NLS-1$

    private WebSocketContainer clientContainer;

    /**
     * Local session
     */
    protected static Session locSession;

    public class DataStreanTransactionRunner implements Runnable {

        private final List<DataPoint> points;

        public DataStreanTransactionRunner(Collection<DataPoint> points) {
            this.points = new ArrayList<>(points);
        }

        public DataStreanTransactionRunner(DataPoint... points) {
            this.points = Arrays.asList(points);
        }

        @Override
        public void run() {
            try {
                _logger.info("\n\n\nClient: Echo test with localhost"); //$NON-NLS-1$
                locSession = getWebSocketContainer().connectToServer(WebSocketClientHandler.class, URI.create(LOCAL_WSSERVER));
                _logger.info("Client: sending... A test string"); //$NON-NLS-1$
                points.forEach(point -> {
                    try {
                        locSession.getBasicRemote().sendObject(point);
                    } catch (IOException | EncodeException ex) {
                        java.util.logging.Logger.getLogger(WebSocketClientContainer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            } catch (DeploymentException | IOException ex) {
                java.util.logging.Logger.getLogger(WebSocketClientContainer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /**
     * Sets up the locat websocket session and sends some data. Sessions that
     * are created here are not explicitly closed because web sockets will close
     * automatically with a timeout. Sessions can be manually closed using a
     * session.close() call.
     *
     * @param context The OSGi Component Context
     * @throws InterruptedException Exception thrown by the sleep function
     * between echo tests
     */
    @Activate
    public void activate(ComponentContext context)
            throws InterruptedException {
        _logger.info("\tActivate client container of the websocket gateway."); //$NON-NLS-1$
    }

    @Deactivate
    public void deactivate(ComponentContext context)
            throws InterruptedException {
        _logger.info("\tDeactivate client container of the websocket gateway."); //$NON-NLS-1$
    }

    public void send(DataPoint... points) {
        _logger.info("\tStart data-ingestion transaction ..."); //$NON-NLS-1
        new Thread(new DataStreanTransactionRunner(points)).start();
        _logger.info("\tData-ingestion transaction ended successfully."); //$NON-NLS-1
    }

    /**
     * Dependency injection of Websocket container
     *
     * @param container Websocket Container used to connect sessions with the
     * server
     */
    @Reference
    protected void setWebSocketContainer(WebSocketContainer container) {
        this.clientContainer = container;
    }

    /**
     * Unset of Websocket container
     *
     * @param container Websocket Container used to connect sessions with the
     * server
     */
    protected void unsetWebSocketContainter(WebSocketContainer container) {
        this.clientContainer = null;
    }

    /**
     * @return the WebSocketContainer
     */
    public WebSocketContainer getWebSocketContainer() {
        return this.clientContainer;
    }
}
