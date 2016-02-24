package com.ge.current.innovation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = RiverFountainApplication.class)
public class HydraRiverFountainApplicationTests {
//
//    private static int messageCount = 100;
//
//    private SimpleDateFormat sdf = new SimpleDateFormat();
//
//    
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    
//    long startTime;
//    long endTime;
//
//    @Before
//    public void recordStartTime() {
//        startTime = System.currentTimeMillis();
//    }
//
//    @After
//    public void recordEndAndExecutionTime() {
//        endTime = System.currentTimeMillis();
//        System.out.println("Exection time: " + (endTime - startTime) + " ms");
//    }
//
//    @Test
//    public void testSingleShotIngestion() {
//        System.out.println("Test single-shot ingestion with " + messageCount + " messages.");
//
//        for (int i = 0; i < messageCount; i++) {
//            DataPoint dp = new DataPoint();
//            dp.setTs(sdf.format(new Date()));
//            dp.setCode("100");
//            dp.setValue(random.nextDouble());
//            rabbitTemplate.convertAndSend("data", dp);
//        }
//
//    }
//    private Random random = new Random();
//
//    @Test
//    public void sendOneMessage() throws Exception {
//        System.out.println("Test ingestion of a message.");
//        DataPoint dp = new DataPoint();
//        dp.setTs(sdf.format(new Date()));
//        dp.setCode("100");
//        dp.setValue(random.nextDouble());
//        rabbitTemplate.convertAndSend("data", dp);
//    }
}
