/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.asset;

import com.ge.lighting.hydra.asset.device.entity.AssetContainerEntity;
import com.ge.lighting.hydra.asset.device.entity.AssetEntity;
import com.ge.lighting.hydra.asset.device.entity.AssetExtractorEntity;
import com.ge.lighting.hydra.asset.device.repository.AssetTreeRepository;
import com.ge.lighting.hydra.asset.model.entity.MetaAttributeEntity;
import com.ge.lighting.hydra.asset.model.entity.MetaTypeEntity;
import com.ge.lighting.hydra.asset.model.entity.MetricEntity;
import com.ge.lighting.hydra.asset.model.repository.IEventEntityRepository;
import com.ge.lighting.hydra.asset.model.repository.MetaEntityRepository;
import com.ge.lighting.hydra.asset.model.repository.MetricEntityRepository;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 *
 * @author predix
 */
@Component
@Profile("local")
public class InitModelAndAssetDataLocal {

    @Autowired
    private IEventEntityRepository eventService;

    @Autowired
    private AssetTreeRepository assetDao;

    @Autowired
    private MetaEntityRepository metaDataDao;

    @Autowired
    private MetricEntityRepository metricRepo;

    MetaData metaData(String icon, String key, String label) {
        return new MetaData(icon, key, label, "");
    }

    MetricEntity metric(String key, String unit) {
        return new MetricEntity(key, unit);
    }

    MetaAttributeEntity createAttribute(MetaTypeEntity owner, MetaData md, MetricEntity metric) {
        final MetaAttributeEntity metaAttributeEntity = new MetaAttributeEntity(owner, metric, UUID.randomUUID(), md);
        metaDataDao.save(metaAttributeEntity);
        return metaAttributeEntity;
    }

    MetaTypeEntity type(MetaData md, MetaAttributeEntity... attributes) {
        final MetaTypeEntity metaTypeEntity = new MetaTypeEntity(UUID.randomUUID(), md);
        metaDataDao.save(metaTypeEntity);
        Arrays.asList(attributes).stream().map((a) -> {
            a.setOwner(metaTypeEntity);
            return a;
        }).forEach(a -> metaDataDao.save(a));
        metaDataDao.save(metaTypeEntity);
        return metaTypeEntity;
    }
    
    GeometryFactory gf = new GeometryFactory();

    private AssetExtractorEntity createExtractor(UUID id, AssetContainerEntity parent, String name, MetaTypeEntity type, Double xLon, Double yLat) {
        final AssetExtractorEntity assetExtractorEntity = new AssetExtractorEntity(id,
                name,
                name + " description",
                parent,
                type,
                true,
                gf.createPoint(new Coordinate(xLon, yLat)));
        this.assetDao.save(assetExtractorEntity);
        return assetExtractorEntity;
    }

    private AssetContainerEntity createContainer(String name, MetaTypeEntity metaType, AssetEntity... children) {
        final AssetContainerEntity assetContainerEntity = new AssetContainerEntity(
                UUID.randomUUID(),
                name,
                name + " description",
                null,
                metaType, true);
        this.assetDao.save(assetContainerEntity);
        Arrays.asList(children).stream().map((a) -> {
            a.setParent(assetContainerEntity);
            return a;
        }).forEach((a) -> {
            this.assetDao.save(a);
        });
        return assetContainerEntity;
    }

    private static final Logger LOG = Logger.getLogger(InitModelAndAssetDataLocal.class.getName());

    @PostConstruct
    public void initData() {
        if (this.metaDataDao.count() == 0) {

            final MetricEntity temeratureMetrix = metric(KEY_TEMPERATURE, "C°");
            final MetricEntity coMetrix = metric(KEY_CO, "ppm");
            final MetricEntity noiseMetrix = metric(KEY_NOISELEVEL, "dB");
            final MetricEntity humidityMetrix = metric(KEY_HUMIDITY, "%");
            final MetricEntity processorUsageMetric = metric(KEY_PROCESSORUSAGE, "%");
            final MetricEntity memoryUsageMetric = metric(KEY_MEMORYUSAGE, "%");
            final MetricEntity storageUsageMetric = metric(KEY_STORAGEUSAGE, "%");

            LOG.info("Create merics ...");
            this.metricRepo.save(temeratureMetrix);
            this.metricRepo.save(processorUsageMetric);
            this.metricRepo.save(memoryUsageMetric);
            this.metricRepo.save(storageUsageMetric);
            this.metricRepo.save(humidityMetrix);
            this.metricRepo.save(noiseMetrix);
            this.metricRepo.save(coMetrix);
            LOG.info("Metrics created.");

            LOG.info("Creeate metamodel");
            final MetaTypeEntity indoorLuminaireType = type(metaData("fa-lightbulb-o", "office-luminaire", "Beltéri okos-lámpa"),
                    createAttribute(null, metaData("fa-fire", KEY_TEMPERATURE, "Hőmérséklet"), temeratureMetrix),
                    createAttribute(null, metaData("fa-cloud", KEY_HUMIDITY, "Páratartalom"), humidityMetrix),
                    createAttribute(null, metaData("fa-diamond", KEY_CO, "CO gáz koncentráció"), coMetrix)
            );

            final MetaTypeEntity outLuminaireType = type(metaData("fa-lightbulb-o", "outdoor-luminaire", "Kültéri okos-lámpa"),
                    createAttribute(null, metaData("fa-wifi", KEY_TEMPERATURE, "Hőmérséklet"), noiseMetrix)
            );
            final MetaTypeEntity faType = type(metaData("fa-tags", "fa", "Beágyazott számítógép"),
                    createAttribute(null, metaData("fa-tag", KEY_PROCESSORUSAGE, "Processzorhasználat"), processorUsageMetric),
                    createAttribute(null, metaData("fa-tag", KEY_MEMORYUSAGE, "Memória kihasználtság"), memoryUsageMetric),
                    createAttribute(null, metaData("fa-tag", KEY_STORAGEUSAGE, "Tárterület kihasználtság"), storageUsageMetric)
            );
            final MetaTypeEntity outdoorContainerType = type(
                    metaData("fa-industry", "outdoor-site", "Kültér")
            );
            final MetaTypeEntity indoorContainerType = type(
                    metaData("fa-building", "office-site", "Beltér")
            );

            this.metaDataDao.save(indoorLuminaireType);
            this.metaDataDao.save(outLuminaireType);
            this.metaDataDao.save(faType);
            this.metaDataDao.save(outdoorContainerType);
            this.metaDataDao.save(indoorContainerType);

            final AssetContainerEntity kfki = createContainer("Kfki", outdoorContainerType,
                    createExtractor(UUID.randomUUID(), null, "Predixberry #0", faType, 0.0, 0.0));
            final AssetContainerEntity ge
                    = createContainer("GE Lighting", outdoorContainerType,
                            createContainer("Innovation Electronical Laboratory", indoorContainerType,
                                    createExtractor(UUID.fromString("48aa829e-81bf-11e5-8bcf-feff819cdc9f"), null, "Luminaire #0", faType, 47.48841662454038, 18.95464564781571),
                                    createExtractor(UUID.fromString("18ea44ff-c40a-4b96-b556-0f8c0cdc0b9e"), null, "Luminaire #1", faType, 47.487474687444866, 18.95462403802867),
                                    createExtractor(UUID.fromString("1f7de3a5-83c0-4501-882f-f52a8ac9debc"), null, "Luminaire #2", faType, 47.488934831756886, 18.954812588955065),
                                    createExtractor(UUID.fromString("3db199e1-2702-4b9a-a9ac-33880054089c"), null, "Luminaire #3", faType, 47.488934831756886, 18.954812588955065),
                                    createExtractor(UUID.fromString("ff78284c-f954-40f9-acd9-47b306c02603"), null, "Luminaire #4", faType, 47.487955249555355, 18.954611999999997)),
                            createContainer("Innovation Warehouse", indoorContainerType,
                                    createExtractor(UUID.randomUUID(), null, "Raspberry PI A", faType, 47.48841662454037, 18.95464564781570),
                                    createExtractor(UUID.randomUUID(), null, "Raspberry PI B", faType, 47.48841662454037, 18.95464564781570),
                                    createExtractor(UUID.randomUUID(), null, "Raspberry PI 2", faType, 47.48841662454037, 18.95464564781570),
                                    createExtractor(UUID.randomUUID(), null, "Odroid", faType, 47.48841662454037, 18.95464564781570),
                                    createExtractor(UUID.randomUUID(), null, "Recopro", faType, 47.48841662454037, 18.95464564781570),
                                    createExtractor(UUID.randomUUID(), null, "Snapdragon", faType, 47.48841662454037, 18.95464564781570)));
            final AssetContainerEntity uranos = createContainer("Hegyvidék", outdoorContainerType,
                    createExtractor(UUID.randomUUID(), null, "Hencz szerszáma", faType, 47.48841662454038, 18.95464564781571));
        }
    }
    
    static final String KEY_STORAGEUSAGE = "storage-usage";
    static final String KEY_MEMORYUSAGE = "memory-usage";
    static final String KEY_PROCESSORUSAGE = "processor-usage";
    static final String KEY_TEMPERATURE = "temperature";
    static final String KEY_HUMIDITY = "humidity";
    static final String KEY_NOISELEVEL = "noise-level";
    static final String KEY_CO = "co";
}
