/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation;

import static com.ge.current.innovation.RiverFountainApplication.ALERT_WS_URI;
import java.io.Serializable;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author lordoftheflies
 */
@Service
public class DataGatewayService {

    private static final Logger LOG = Logger.getLogger(DataGatewayService.class.getName());

    @Value("${node}")
    private String nodeId;
    @Value("${dataingestion.protocol}://${dataingestion.uri}")
    private String dataIngestionUrl;

    private SimpleDateFormat sdf = new SimpleDateFormat();

    public void send(DataPoint... messages) throws Exception {

        WebSocketClient dataIngestionWebsocketClient = new WebSocketClient();
        DataIngestionConsumerSocket dataIngestionConsumerSocket = new DataIngestionConsumerSocket();
        LOG.log(Level.INFO, "Node[{0}] send messages ...", nodeId);
        LOG.log(Level.INFO, "\tStart websocket client to {0} ...", dataIngestionUrl);
        dataIngestionWebsocketClient.start();
        URI dataIngestionUri = new URI(dataIngestionUrl);
        int i = 0;
        try (Session session = dataIngestionWebsocketClient.connect(dataIngestionConsumerSocket, dataIngestionUri, new ClientUpgradeRequest()).get()) {
            LOG.log(Level.INFO, "\tConnecting to data-ingestion websocket-server[{0}] ...", dataIngestionUrl);
            for (DataPoint msg : messages) {
                msg.setValue((double)i);
                session.getRemote().sendString(msg.toString());
                LOG.log(Level.INFO, "\t\tSend {0} message: {1}", new Object[]{i++, msg});
            }
            dataIngestionConsumerSocket.awaitClose(10, TimeUnit.SECONDS);
            LOG.log(Level.INFO, "\tClose session.");
        } finally {
            try {
                dataIngestionWebsocketClient.stop();
            } catch (Exception e) {
                e.printStackTrace();
                LOG.log(Level.INFO, "\tStop websocket client.");
            }
        }
    }
}
