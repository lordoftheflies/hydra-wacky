package com.ge.current.innovation.storage.jpa.boot;

import com.ge.current.innovation.storage.jpa.boot.HydraStorageApplication;
import com.ge.current.innovation.storage.jpa.dal.DataPointRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HydraStorageApplication.class)
public class HydraStorageApplicationTests {

    
    @Test
    public void contextLoads() {
    }

}
