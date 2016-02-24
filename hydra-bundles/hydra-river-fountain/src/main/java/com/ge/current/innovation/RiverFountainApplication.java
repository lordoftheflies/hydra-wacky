package com.ge.current.innovation;

import java.net.URI;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RiverFountainApplication implements CommandLineRunner {

    private static final Logger LOG = Logger.getLogger(RiverFountainApplication.class.getName());

    public static final String ALERT_WS_URI = "ws://hydra-websocket-dataingestion.run.aws-usw02-pr.ice.predix.io/alert";
    public static final String DATA_WS_URI = "ws://hydra-websocket-dataingestion.run.aws-usw02-pr.ice.predix.io/data";

//    public static void main(String[] args) {
//        SpringApplication.run(RiverFountainApplication.class, args);
//    }
    public static void main(String[] args) {
//        StandardWebSocketClient simpleWebSocketClient = new StandardWebSocketClient();
//List<Transport> transports = new ArrayList<>(1);
//transports.add(new WebSocketTransport(simpleWebSocketClient));
//
//SockJsClient sockJsClient = new SockJsClient(transports);
//sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());
//
//StompMessageHandler messageHandler = new StompMessageHandler();
//StompWebSocketHandler websocketHandler = new StompWebSocketHandler(messageHandler, new StringMessageConverter());
//
//WebSocketConnectionManager manager = new WebSocketConnectionManager(sockJsClient, websocketHandler, "ws://localhost:8080/stomp");
//
//manager.start();
//
//System.in.read();
//        
        String destUri = "ws://hydra-websocket-dataingestion.run.aws-usw02-pr.ice.predix.io";
//        String destUri = "ws://localhost:9092/app/data";
//        if (args.length > 0) {
//            destUri = args[0];
//        }

        WebSocketClient client = new WebSocketClient();
        WebSocketClient client2 = new WebSocketClient();
        SimpleAlertSocket socket = new SimpleAlertSocket();
        DataIngestionSocket dataSocket = new DataIngestionSocket();
        try {
            client.start();
            URI echoUri = new URI(ALERT_WS_URI);

            Future<Session> fut = client.connect(socket, echoUri, new ClientUpgradeRequest());
            try (Session session = fut.get()) {
                for (int i = 0; i < 1000; i++) {
                    session.getRemote().sendString("Alert-" + i);
                    LOG.log(Level.INFO, "Alert-{0}", i);
                }
            }
            
//            ClientUpgradeRequest request = new ClientUpgradeRequest();
//            client2.connect(dataSocket, new URI(DATA_WS_URI), request);
//            System.out.printf("Connecting to : %s%n", echoUri);
//
//            dataSocket.awaitClose(5, TimeUnit.SECONDS);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//    
//    private SimpleDateFormat sdf = new SimpleDateFormat();
//    

    @Override
    public void run(String... strings) throws Exception {

//        Random random = new Random();
//        for (int i = 0; i < 100; i++) {
//            DataPoint dp = new DataPoint();
//            dp.setTs(sdf.format(new Date()));
//            dp.setCode("100");
//            dp.setValue(random.nextDouble());
//            rabbitTemplate.convertAndSend("data", dp);
//        }
    }
}
