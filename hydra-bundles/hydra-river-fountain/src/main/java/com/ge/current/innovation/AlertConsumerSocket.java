/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;

/**
 *
 * @author lordoftheflies
 */
/**
 * Basic Echo Client Socket
 */
@WebSocket
public class SimpleAlertSocket {

    private static final Logger LOG = Logger.getLogger(SimpleAlertSocket.class.getName());

    private final CountDownLatch closeLatch;

    @SuppressWarnings("unused")
    private Session session;

    public SimpleAlertSocket() {
        this.closeLatch = new CountDownLatch(1);
    }

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.printf("Alert-nofification connection closed: %d - %s%n", statusCode, reason);
        this.session = null;
        this.closeLatch.countDown();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.printf("Alert-nofification got connect: %s%n", session);
        this.session = session;
//        try {
////            fut.get(2, TimeUnit.SECONDS);
////            fut = session.getRemote().sendStringByFuture("Thanks for the conversation.");
////            fut.get(2, TimeUnit.SECONDS);
////            session.close(StatusCode.NORMAL, "I'm done");
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) {
//        try {
            System.out.printf("Alert-nofification request: %s%n", msg);
//            Future<Void> fut = session.getRemote().sendStringByFuture(msg);
//            fut.get(10, TimeUnit.SECONDS);
//        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
//            LOG.log(Level.SEVERE, "Aler-notification communication exception.", ex);
//        }
    }
}
