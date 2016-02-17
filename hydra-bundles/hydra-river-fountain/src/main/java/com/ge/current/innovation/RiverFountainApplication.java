package com.ge.current.innovation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RiverFountainApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RiverFountainApplication.class, args);
    }
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    private SimpleDateFormat sdf = new SimpleDateFormat();
    
    @Override
    public void run(String... strings) throws Exception {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            DataPoint dp = new DataPoint();
            dp.setTs(sdf.format(new Date()));
            dp.setCode("100");
            dp.setValue(random.nextDouble());
            rabbitTemplate.convertAndSend("data", dp);
        }
    }
}
