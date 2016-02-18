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

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

import com.ge.lighting.hydra.m2m.common.DataPoint;
import com.ge.lighting.hydra.m2m.common.Edge;
import com.ge.lighting.hydra.m2m.common.Machine;
import com.ge.lighting.hydra.m2m.common.Sensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

import javax.websocket.EncodeException;

/**
 *
 * @author Predix Machine Sample
 */
@Component(name = WebSocketClientContainerSample.SERVICE_PID)
public class WebSocketClientContainerSample {

    /**
     * Create logger to report errors, warning massages, and info messages
     * (runtime Statistics)
     */
    protected static Logger _logger = LoggerFactory.getLogger(WebSocketClientContainerSample.class);

    /**
     * SERVICE_PID String used as the component service name
     */
    protected final static String SERVICE_PID = "com.ge.dspmicro.wsclient.sample";                            //$NON-NLS-1$

    private static final String LOCAL_WSSERVER = "ws://localhost:8098/data/channel";
    private String getLocalWsServerRealtime() { return "ws://localhost:8098/data/" + machine.getId() + "/realtime"; }
    private static final String LOCAL_WSSERVER_ASSET = "ws://localhost:8183/asset";                  //$NON-NLS-1$

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

    private Machine machine;

    public WebSocketClientContainerSample() {
        machine = new Machine();
        UUID machineId = UUID.randomUUID();
        UUID edgeId0 = UUID.randomUUID();
        UUID edgeId1 = UUID.randomUUID();
        UUID edgeId2 = UUID.randomUUID();
        UUID edgeId3 = UUID.randomUUID();
        UUID sensorId0 = UUID.randomUUID();
        UUID sensorId1 = UUID.randomUUID();
        UUID sensorId2 = UUID.randomUUID();
        UUID sensorId3 = UUID.randomUUID();
        UUID sensorId4 = UUID.randomUUID();
        UUID sensorId5 = UUID.randomUUID();
        UUID sensorId6 = UUID.randomUUID();
        UUID sensorId7 = UUID.randomUUID();
        machine.setId(machineId);
        machine.setChildren(Arrays.asList(new Edge[]{
            new Edge(edgeId0, machineId, 19.081575572490692, 47.57675008274512, 0.0, null, Arrays.asList(new Sensor[]{
                new Sensor(sensorId0, edgeId0, DIMENSION_ENVIRONMENT_TEMPERATURE, null),
                new Sensor(sensorId1, edgeId0, DIMENSION_UV_RADIATION, null)
            })),
            new Edge(edgeId1, machineId, 19.081637263298035, 47.57675189216864, 0.0, null, Arrays.asList(new Sensor[]{
                new Sensor(sensorId2, edgeId1, DIMENSION_MOTION_COUNT, null),
                new Sensor(sensorId2, edgeId1, DIMENSION_NOISE_AVERAGE, null),
                new Sensor(sensorId3, edgeId1, DIMENSION_CO_CONTENCTRATION, null),
                new Sensor(sensorId4, edgeId1, DIMENSION_LIGHTING_LEVEL, null)
            })),
            new Edge(edgeId2, machineId, 19.081572890281674, 47.57672113196058, 0.0, null, Arrays.asList(new Sensor[]{
                new Sensor(sensorId5, edgeId1, DIMENSION_O3_CONCENTRATION, null)
            })),
            new Edge(edgeId3, machineId, 19.08163458108902, 47.57672113196058, 0.0, null, Arrays.asList(new Sensor[]{
                new Sensor(sensorId6, edgeId1, DIMENSION_MEMORY_USAGE, null),
                new Sensor(sensorId7, edgeId1, DIMENSION_PROCESSOR_USAGE, null)
            }))
        }));
    }
    public static final String DIMENSION_PROCESSOR_USAGE = "processor-usage";
    public static final String DIMENSION_MEMORY_USAGE = "memory-usage";
    public static final String DIMENSION_O3_CONCENTRATION = "o3-concentration";
    public static final String DIMENSION_LIGHTING_LEVEL = "lighting-level";
    public static final String DIMENSION_CO_CONTENCTRATION = "co-contenctration";
    public static final String DIMENSION_NOISE_AVERAGE = "noise-average";
    public static final String DIMENSION_MOTION_COUNT = "motion-count";
    public static final String DIMENSION_UV_RADIATION = "uv-radiation";
    public static final String DIMENSION_ENVIRONMENT_TEMPERATURE = "environment-temperature";

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
    protected void activate(ComponentContext context) throws InterruptedException {

        _logger.info("Hydra Mock Data-extractor websocket client: Activate machine.");
        // Best practice is to do these in a thread since long running tasks should not take place in the activate.
        Runnable realTimeRunner = new RealtimeDataCollectionRunner();
        Runnable batchRunner = new MsgDataCollectionRunner();
        
        terminated = false;
        new Thread(batchRunner).start();
        new Thread(realTimeRunner).start();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        terminated = true;
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

    private static final Random dataGenerator = new Random();

    private static boolean terminated = false;

    public class MsgDataCollectionRunner implements Runnable {

        public MsgDataCollectionRunner() {
        }

        private List<DataPoint> generateEdgePoints(Edge edge) {
            List<DataPoint> points = new ArrayList<>();
            edge.getChildren().stream().forEach((sensor) -> {
                points.add(new DataPoint(sensor.getKey(), new Date(), edge.getId(), dataGenerator.nextDouble()));
            });
            return points;
        }

        @Override
        public void run() {
            while (!terminated) {

                try {
                    _logger.info("Hydra Mock Data-extractor websocket client: Generate stream ..."); //$NON-NLS-1$
                    locSession = getWebSocketContainer().connectToServer(WsClientReatimeMsgHandler.class, URI.create(LOCAL_WSSERVER));
                    for (Edge children : machine.getChildren()) {
                        for (DataPoint p : generateEdgePoints(children)) {
                            locSession.getBasicRemote().sendObject(p);
                            _logger.info("Hydra Mock Data-extractor websocket client: Send data " + p); //$NON-NLS-1$
                        }
                    }
                    Thread.sleep(10000);
                } catch (DeploymentException | IOException | EncodeException e) {
                    _logger.error("Hydra Mock Data-extractor websocket client: Failed to send messages to server", e); //$NON-NLS-1$
                } catch (InterruptedException ex) {
                    _logger.error("Data generation thread interrupted.", ex);
                }

                _logger.info("Hydra Mock Data-extractor websocket client: Generate stream ...");
            }
        }
    }
    
    public class RealtimeDataCollectionRunner implements Runnable {

        public RealtimeDataCollectionRunner() {
        }

        private List<DataPoint> generateEdgePoints(Edge edge) {
            List<DataPoint> points = new ArrayList<>();
            for (Sensor sensor : edge.getChildren()) {
                points.add(new DataPoint(sensor.getKey(), new Date(), edge.getId(), dataGenerator.nextDouble()));
            }
            return points;
        }

        @Override
        public void run() {
            while (!terminated) {

                try {
                    _logger.info("Hydra Mock Data-extractor websocket client: Generate stream ..."); //$NON-NLS-1$
                    locSession = getWebSocketContainer().connectToServer(WsClientBatchMsgHandler.class, URI.create(getLocalWsServerRealtime()));
                    for (Edge children : machine.getChildren()) {
                        for (DataPoint p : generateEdgePoints(children)) {
                            locSession.getBasicRemote().sendText(p.getKey() + "=" + p.getValue());
                            _logger.info("Hydra Mock Data-extractor websocket client: Send data " + p); //$NON-NLS-1$
                        }
                    }
                    Thread.sleep(10000);
                } catch (DeploymentException | IOException e) {
                    _logger.error("Hydra Mock Data-extractor websocket client: Failed to send messages to server", e); //$NON-NLS-1$
                } catch (InterruptedException ex) {
                    _logger.error("Data generation thread interrupted.", ex);
                }

                _logger.info("Hydra Mock Data-extractor websocket client: Generate stream ...");
            }
        }
    }
}
