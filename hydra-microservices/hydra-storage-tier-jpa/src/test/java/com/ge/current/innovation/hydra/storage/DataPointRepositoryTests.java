package com.ge.current.innovation.hydra.storage;

import com.ge.current.innovation.hydra.storage.boot.HydraStorageApplication;
import com.ge.current.innovation.hydra.storage.entities.AssetEntity;
import com.ge.current.innovation.hydra.storage.entities.AssetMeterEntity;
import com.ge.current.innovation.hydra.storage.entities.DataPointEntity;
import com.ge.current.innovation.hydra.storage.entities.MeterEntity;
import com.ge.current.innovation.hydra.storage.dal.AssetMeterRepository;
import com.ge.current.innovation.hydra.storage.dal.AssetRepository;
import com.ge.current.innovation.hydra.storage.dal.DataPointRepository;
import com.ge.current.innovation.hydra.storage.dal.MeterRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HydraStorageApplication.class)
public class DataPointRepositoryTests {

    @Autowired
    private DataPointRepository dpRepo;
    @Autowired
    private AssetMeterRepository assetMeterRepo;
    @Autowired
    private MeterRepository meterRepo;
    @Autowired
    private AssetRepository assetRepo;

    private static int messageCount = 100;

    long startTime;
    long endTime;

    @Before
    public void recordStartTime() {
        startTime = System.currentTimeMillis();
    }

    @After
    public void recordEndAndExecutionTime() {
        endTime = System.currentTimeMillis();
        System.out.println("Exection time: " + (endTime - startTime) + " ms");
    }

    @Test
    public void testIngestion() {
        System.out.println("Test ingestion of a message.");

        dpRepo.save(new DataPointEntity());
    }

    @Test
    public void testSingleShotIngestion() {
        System.out.println("Test single-shot ingestion with " + messageCount + " messages.");

        MeterEntity meterEntity = new MeterEntity();
        meterEntity.setName("testSingleShotIngestion-meter-name");
        meterEntity.setUom("m/s");
        meterRepo.save(meterEntity);

        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setFriendlyName("testSingleShotIngestion-asset-name");
        assetEntity.setDescription("testSingleShotIngestion-asset-description");
        assetEntity.setUri("/asset/" + assetEntity.getFriendlyName());
        assetRepo.save(assetEntity);

        AssetMeterEntity assetMeterEntity = new AssetMeterEntity();
        assetMeterEntity.setMeter(meterEntity);
        assetMeterEntity.setAsset(assetEntity);
        assetMeterRepo.save(assetMeterEntity);

        for (int i = 0; i < messageCount; i++) {
            DataPointEntity dp = new DataPointEntity();
            dp.setTimeStamp(new Date());

            dp.setAssetMeter(assetMeterEntity);
            dpRepo.save(dp);
        }
    }

    @Test
    public void testBatchIngestion() {
        System.out.println("Test batch ingestion with " + messageCount + " messages.");

        MeterEntity meterEntity = new MeterEntity();
        meterEntity.setName("testBatchIngestion-meter-name");
        meterEntity.setUom("m/s");
        meterRepo.save(meterEntity);

        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setFriendlyName("testBatchIngestion-asset-name");
        assetEntity.setDescription("testBatchIngestion-asset-description");
        assetEntity.setUri("/asset/" + assetEntity.getFriendlyName());
        assetRepo.save(assetEntity);

        AssetMeterEntity assetMeterEntity = new AssetMeterEntity();
        assetMeterEntity.setMeter(meterEntity);
        assetMeterEntity.setAsset(assetEntity);
        assetMeterRepo.save(assetMeterEntity);

        List<DataPointEntity> points = new ArrayList<>();
        for (int i = 0; i < messageCount; i++) {
            DataPointEntity dp = new DataPointEntity();
            dp.setTimeStamp(new Date());
            dp.setAssetMeter(assetMeterEntity);
            points.add(dp);
        }

        dpRepo.save(points);
    }

}
