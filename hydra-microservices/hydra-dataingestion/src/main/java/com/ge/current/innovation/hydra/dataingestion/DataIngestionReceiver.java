/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.hydra.dataingestion;

import com.ge.current.innovation.DataPoint;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author lordoftheflies
 */
public class DataIngestionReceiver {

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(DataPoint message) {
        System.out.println("Received <" + message + ">");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    
}
