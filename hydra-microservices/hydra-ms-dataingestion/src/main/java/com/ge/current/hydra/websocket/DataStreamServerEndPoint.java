package com.ge.current.hydra.websocket;

import com.ge.current.innovation.DataPoint;
import com.ge.current.innovation.utils.JsonUtils;
import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ServerEndpoint(value = "/data/{nodeId}/ingest", configurator = SpringEndpointConfigurator.class)
public class DataStreamServerEndPoint {

    private static final Logger LOG = Logger.getLogger(DataStreamServerEndPoint.class.getName());

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AssetHandler assetHandler;

    @OnOpen
    public void onOpen(@PathParam(value = "nodeId") String nodeId, final Session session, EndpointConfig ec) {
        LOG.log(Level.INFO, "Data-stream session opened for node Id : {0} : {1}", new Object[]{
            nodeId,
            session.getId()
        });

        if (!assetHandler.exist(nodeId)) {
            LOG.log(Level.INFO, "Attach connection to MQ.");
            assetHandler.attachNode(nodeId);
            
        }

//        appContext.getBeanFactory().registerSingleton("foo", new Queue("foo"));
// Create asset if not exists
    }

    @OnClose
    public void onClose(@PathParam(value = "nodeId") String nodeId, Session session, CloseReason closeReason) {
        LOG.log(Level.SEVERE, "Data-stream session {0} closed because of {1}", new Object[]{
            session.getId(),
            closeReason.getCloseCode() == CloseReason.CloseCodes.NORMAL_CLOSURE ? " client " + nodeId + " ended the transaction" : closeReason.getReasonPhrase()
        });
    }

    @OnError
    public void onError(@PathParam(value = "nodeId") String nodeId, Session session, Throwable t) {
        LOG.log(Level.SEVERE, "Data-stream session {0} to {1} closed with error because of {2}", new Object[]{
            session.getId(),
            nodeId,
            t.getMessage()
        });
    }

    @OnMessage
    public void onMessage(@PathParam(value = "nodeId") String nodeId, String message, Session session) throws IOException, InterruptedException {
        try {
            LOG.log(Level.INFO, "Request << WS-CLIENT[node={0}]: {1}", new Object[]{nodeId, message});

            LOG.log(Level.INFO, "Message >> MQ-SERVER[queue={0}]: {1}", new Object[]{nodeId, message});
            
            DataPoint dp = new JsonUtils().read(DataPoint.class, message);
            dp.setTs(new SimpleDateFormat().format(new Date()));
            
            rabbitTemplate.convertAndSend(nodeId, dp);

//            final String ack = (String) rabbitTemplate.receiveAndConvert(nodeId);
//            LOG.log(Level.INFO, "Acknowledgement << MQ-SERVER: {0}", ack);

            LOG.log(Level.INFO, "Response >> WS-CLIENT[node={0}]: {1}", new Object[]{nodeId, message});
            session.getBasicRemote().sendText("SUCCESS (" + session.getId() + ")");

        } catch (AmqpException | IOException ex) {
            final String ack = "Failed to ingest message.";
            LOG.log(Level.SEVERE, ack, ex);
            session.getBasicRemote().sendText(ack);
        }
    }

    private JsonObject findJsonObjectByName(JsonArray nodes, String pNodeName) {
        for (int i = 0; i < nodes.size(); i++) {
            JsonObject node = (JsonObject) nodes.get(i);
            String nodeName = node.get("name").getAsString();
            if (pNodeName.equalsIgnoreCase(nodeName.trim())) {
                return node;
            }
        }
        return null;
    }

}
