package com.ge.lighting.hydra.datastore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ge.lighting.hydra.datastore.entity.DataKey;
import com.ge.lighting.hydra.datastore.entity.DataPointEntity;
import com.ge.lighting.hydra.datastore.repository.DataRepository;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;

@Component
@Profile("dev")
public class InitSensorDataDev {

    @Autowired
    private DataRepository dataDao;

    private static final Logger LOG = Logger.getLogger(InitSensorDataDev.class.getName());

    static final String KEY_HB = "heartbeat";
    static final String KEY_TEST = "db-test";

    static final UUID TEST_CONTAINER_0 = UUID.fromString("be214564-92e8-11e5-8994-feff819cdc9f");
    static final UUID TEST_EXTRACTOR_0 = UUID.fromString("be21496a-92e8-11e5-8994-feff819cdc9f");
    static final UUID TEST_EXTRACTOR_1 = UUID.fromString("be214b68-92e8-11e5-8994-feff819cdc9f");
    static final UUID TEST_EXTRACTOR_2 = UUID.fromString("be214d3e-92e8-11e5-8994-feff819cdc9f");
    static final UUID TEST_EXTRACTOR_3 = UUID.fromString("be214f0a-92e8-11e5-8994-feff819cdc9f");

    @PostConstruct
    public void initData() {
        if (dataDao.count() == 0) {
            Random r = new Random();
            for (int i = 0; i < 10; i++) {
                dataDao.save(new DataPointEntity(new DataKey(KEY_TEST, new Date(), TEST_EXTRACTOR_0), r.nextDouble()));
                dataDao.save(new DataPointEntity(new DataKey(KEY_TEST, new Date(), TEST_EXTRACTOR_1), r.nextDouble()));
                dataDao.save(new DataPointEntity(new DataKey(KEY_TEST, new Date(), TEST_EXTRACTOR_2), r.nextDouble()));
                dataDao.save(new DataPointEntity(new DataKey(KEY_TEST, new Date(), TEST_EXTRACTOR_3), r.nextDouble()));
            }
        }
    }

}
