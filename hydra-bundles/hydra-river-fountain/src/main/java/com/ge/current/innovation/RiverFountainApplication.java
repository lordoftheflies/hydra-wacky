package com.ge.current.innovation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RiverFountainApplication implements CommandLineRunner {

    private static final Logger LOG = Logger.getLogger(RiverFountainApplication.class.getName());

    public static final String ALERT_WS_URI = "ws://localhost:9092/alert";
    public static final String DATA_WS_URI = "ws://localhost:9092/data";

    public static void main(String[] args) {
        SpringApplication.run(RiverFountainApplication.class, args);
    }
//    public static void main(String[] args) {
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
////        
//        String destUri = "ws://hydra-websocket-dataingestion.run.aws-usw02-pr.ice.predix.io";
////        String destUri = "ws://localhost:9092/app/data";
////        if (args.length > 0) {
////            destUri = args[0];
////        }
//
//        WebSocketClient alertWebsocketClient = new WebSocketClient();
//        AlertConsumerSocket alertConsumerSocket = new AlertConsumerSocket();
//
//        WebSocketClient dataIngestionWebsocketClient = new WebSocketClient();
//        DataIngestionConsumerSocket data = new DataIngestionConsumerSocket();
//        try {
//            dataIngestionWebsocketClient.start();
//            URI echoUri = new URI(ALERT_WS_URI);
//
//            Future<Session> fut = dataIngestionWebsocketClient.connect(alertConsumerSocket, echoUri, new ClientUpgradeRequest());
//            try (Session session = fut.get()) {
//                for (int i = 0; i < 1000; i++) {
//                    session.getRemote().sendString("Alert-" + i);
//                    LOG.log(Level.INFO, "Alert-{0}", i);
//                }
//            }
//
////            ClientUpgradeRequest request = new ClientUpgradeRequest();
////            client2.connect(dataSocket, new URI(DATA_WS_URI), request);
////            System.out.printf("Connecting to : %s%n", echoUri);
////
////            dataSocket.awaitClose(5, TimeUnit.SECONDS);
//        } catch (Throwable t) {
//            t.printStackTrace();
//        } finally {
//            try {
//                dataIngestionWebsocketClient.stop();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
////    @Autowired
////    private RabbitTemplate rabbitTemplate;
////    
////    private SimpleDateFormat sdf = new SimpleDateFormat();
////    

    @Autowired
    private DataGatewayService dataIngestionGatewayService;
    @Autowired
    private CsvExternalizationService csvExternalizationService;

    protected void generateData(int num, String file) throws FileNotFoundException, IOException {
        List<DataPoint> points = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < num; i++) {
            points.add(new DataPoint(new Date().toString(), "kacsa", r.nextDouble()));
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            csvExternalizationService.serialize(points, fos, DataPoint.class);
        }
    }

    @Override
    public void run(String... strings) throws Exception {

//        WebSocketClient alertWebsocketClient = new WebSocketClient();
//        AlertConsumerSocket alertConsumerSocket = new AlertConsumerSocket();
//
//        try {
//            alertWebsocketClient.start();
//            URI echoUri = new URI(ALERT_WS_URI);
//
//            Future<Session> fut = alertWebsocketClient.connect(alertConsumerSocket, echoUri, new ClientUpgradeRequest());
//            try (Session session = fut.get()) {
//                for (int i = 0; i < 1000; i++) {
//                    session.getRemote().sendString("Alert-" + i);
//                    LOG.log(Level.INFO, "Alert-{0}", i);
//                }
//            }
//
////            ClientUpgradeRequest request = new ClientUpgradeRequest();
////            client2.connect(dataSocket, new URI(DATA_WS_URI), request);
////            System.out.printf("Connecting to : %s%n", echoUri);
////
////            dataSocket.awaitClose(5, TimeUnit.SECONDS);
//        } catch (Throwable t) {
//            t.printStackTrace();
//        } finally {
//            try {
//                alertWebsocketClient.stop();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        
//        
//         WebSocketClient dataWebsocketClient = new WebSocketClient();
//        DataIngestionConsumerSocket dataConsumerSocket = new DataIngestionConsumerSocket();
//
//        try {
//            dataWebsocketClient.start();
//            URI echoUri = new URI(DATA_WS_URI);
//
//            Future<Session> fut = dataWebsocketClient.connect(dataConsumerSocket, echoUri, new ClientUpgradeRequest());
//            try (Session session = fut.get()) {
//                for (int i = 0; i < 1000; i++) {
//                    session.getRemote().sendString("Data-" + i);
//                    LOG.log(Level.INFO, "Data-{0}", i);
//                }
//            }
//        } catch (Throwable t) {
//            t.printStackTrace();
//        } finally {
//            try {
//                dataWebsocketClient.stop();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        strings = new String[]{"test.csv"};

        this.generateData(5, strings[0]);

        try (FileInputStream fis = new FileInputStream(strings[0])) {
            List<DataPoint> messages = csvExternalizationService.parse(fis, DataPoint.class).stream()
                    //                    .map((Object t) -> t.toString())
                    .collect(Collectors.toList());
            DataPoint[] pst = new DataPoint[messages.size()];
            dataIngestionGatewayService.send(messages.toArray(pst));
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
}
