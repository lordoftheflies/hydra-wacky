/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 *
 * @author lordoftheflies
 */
/**
 * Basic Echo Client Socket
 */
@WebSocket
public class DataIngestionConsumerSocket {

    private final CountDownLatch closeLatch;

    @SuppressWarnings("unused")
    private Session session;

    public DataIngestionConsumerSocket() {
        this.closeLatch = new CountDownLatch(1);
    }

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.printf("Data-ingestion connection closed: %d - %s%n", statusCode, reason);
        this.session = null;
        this.closeLatch.countDown();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.printf("Data-ingestion got connect: %s%n", session);
//        this.session = session;
//        try {
//            Future<Void> fut;
//            fut = session.getRemote().sendStringByFuture("Hello");
//            fut.get(2, TimeUnit.SECONDS);
//            fut = session.getRemote().sendStringByFuture("Thanks for the conversation.");
//            fut.get(2, TimeUnit.SECONDS);
////            session.close(StatusCode.NORMAL, "I'm done");
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        System.out.printf("Data-ingestion got msg: %s%n", msg);
    }
}
