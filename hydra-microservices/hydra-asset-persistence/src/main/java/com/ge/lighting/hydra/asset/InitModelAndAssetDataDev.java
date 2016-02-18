package com.ge.lighting.hydra.asset;

import com.ge.lighting.hydra.asset.device.entity.AssetContainerEntity;
import com.ge.lighting.hydra.asset.device.entity.AssetEntity;
import com.ge.lighting.hydra.asset.device.entity.AssetExtractorEntity;
import com.ge.lighting.hydra.asset.device.repository.AssetTreeRepository;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ge.lighting.hydra.asset.model.entity.MetricEntity;
import com.ge.lighting.hydra.asset.model.entity.MetaAttributeEntity;
import com.ge.lighting.hydra.asset.model.entity.MetaTypeEntity;
import com.ge.lighting.hydra.asset.model.repository.IEventEntityRepository;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.ge.lighting.hydra.asset.model.repository.MetricEntityRepository;
import com.ge.lighting.hydra.asset.model.repository.MetaEntityRepository;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;

@Component
@Profile("dev")
public class InitModelAndAssetDataDev {

    @Autowired
    private IEventEntityRepository eventService;

    @Autowired
    private AssetTreeRepository assetDao;

    @Autowired
    private MetaEntityRepository metaDataDao;

    @Autowired
    private MetricEntityRepository metricRepo;

    MetaData metaData(String icon, String key, String label, String description) {
        return new MetaData(icon, key, label, description);
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

    private AssetExtractorEntity createExtractor(AssetContainerEntity parent, String name, MetaTypeEntity type, Double xLon, Double yLat) {
        final AssetExtractorEntity assetExtractorEntity = createExtractor(UUID.randomUUID(), parent, name, type, xLon, yLat);
        return assetExtractorEntity;
    }

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

    private void setBounds(AssetContainerEntity ge, Coordinate... coordinates) {
        ge.setBounds(gf.createPolygon(coordinates));
    }

    private AssetContainerEntity createContainer(String name, MetaTypeEntity metaType, AssetEntity... children) {
        final AssetContainerEntity assetContainerEntity = this.createContainer(UUID.randomUUID(), name, metaType, children);
        return assetContainerEntity;
    }

    private AssetContainerEntity createContainer(UUID id, String name, MetaTypeEntity metaType, AssetEntity... children) {
        final AssetContainerEntity assetContainerEntity = new AssetContainerEntity(
                id,
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

    private static final Logger LOG = Logger.getLogger(InitModelAndAssetDataDev.class.getName());

    static final UUID TEST_CONTAINER_0 = UUID.fromString("be214564-92e8-11e5-8994-feff819cdc9f");
    static final UUID TEST_EXTRACTOR_0 = UUID.fromString("be21496a-92e8-11e5-8994-feff819cdc9f");
    static final UUID TEST_EXTRACTOR_1 = UUID.fromString("be214b68-92e8-11e5-8994-feff819cdc9f");
    static final UUID TEST_EXTRACTOR_2 = UUID.fromString("be214d3e-92e8-11e5-8994-feff819cdc9f");
    static final UUID TEST_EXTRACTOR_3 = UUID.fromString("be214f0a-92e8-11e5-8994-feff819cdc9f");

    @PostConstruct
    public void initData() {
        final MetricEntity temeratureMetrix = metric(KEY_TEMPERATURE, "C°");
        final MetricEntity coMetrix = metric(KEY_CO, "ppm");
        final MetricEntity noiseMetrix = metric(KEY_NOISELEVEL, "dB");
        final MetricEntity humidityMetrix = metric(KEY_HUMIDITY, "%");
        final MetricEntity processorUsageMetric = metric(KEY_PROCESSORUSAGE, "%");
        final MetricEntity memoryUsageMetric = metric(KEY_MEMORYUSAGE, "%");
        final MetricEntity storageUsageMetric = metric(KEY_STORAGEUSAGE, "%");
        final MetricEntity heartBeatMetric = metric(KEY_HB, "db");
        final MetricEntity testMetric = metric(KEY_TEST, "kacsa");

        LOG.info("Create merics ...");
        this.metricRepo.save(temeratureMetrix);
        this.metricRepo.save(processorUsageMetric);
        this.metricRepo.save(memoryUsageMetric);
        this.metricRepo.save(storageUsageMetric);
        this.metricRepo.save(humidityMetrix);
        this.metricRepo.save(noiseMetrix);
        this.metricRepo.save(coMetrix);
        this.metricRepo.save(heartBeatMetric);
        this.metricRepo.save(testMetric);
        LOG.info("Metrics created.");

        LOG.info("Create metamodel");
        final MetaTypeEntity testType = type(metaData("fa-tags", "location", "Teszt végpont", "Teszt végponti adatgyűjtő."),
                createAttribute(null, metaData("fa-fire", KEY_HB, "Heartbeat", "Végpont tesztelés."), heartBeatMetric),
                createAttribute(null, metaData("fa-cloud", KEY_TEST, "Teszt KPI", "Model tesztelés."), testMetric)
        );
        final MetaTypeEntity testType2 = type(metaData("fa-tags", "site", "Teszt konténer", "Teszt konténer."));

        final MetaTypeEntity indoorLuminaireType = type(metaData("fa-lightbulb-o", "office-luminaire", "Beltéri okos-lámpa", "Szenzoros adatgyűjtővel felszerelt vezérelhető lámpa."),
                createAttribute(null, metaData("fa-fire", KEY_TEMPERATURE, "Hőmérséklet", "Belétri hőmérséklet."), temeratureMetrix),
                createAttribute(null, metaData("fa-cloud", KEY_HUMIDITY, "Páratartalom", "Beltéri práatartalom."), humidityMetrix),
                createAttribute(null, metaData("fa-diamond", KEY_CO, "CO gáz koncentráció", "Beltéri szén-monoxid koncentráció."), coMetrix)
        );

        final MetaTypeEntity outLuminaireType = type(metaData("fa-lightbulb-o", "outdoor-luminaire", "Kültéri okos-lámpa", "Szenzoros adatgyűjtővel felszerelt kültéri lámpa."),
                createAttribute(null, metaData("fa-wifi", KEY_TEMPERATURE, "Hőmérséklet", "Kültéri hőmérséklet."), noiseMetrix)
        );
        final MetaTypeEntity faType = type(metaData("fa-tags", "fa", "Beágyazott számítógép", "Végkészülékek, diagnosztikai célból."),
                createAttribute(null, metaData("fa-tag", KEY_PROCESSORUSAGE, "Processzorhasználat", "Processzo terheltsége."), processorUsageMetric),
                createAttribute(null, metaData("fa-tag", KEY_MEMORYUSAGE, "Memória kihasználtság", "Memória terheltsége."), memoryUsageMetric),
                createAttribute(null, metaData("fa-tag", KEY_STORAGEUSAGE, "Tárterület kihasználtság", "Felhasznált tárterület aránya az összeshez."), storageUsageMetric)
        );
        final MetaTypeEntity outdoorContainerType = type(
                metaData("fa-industry", "outdoor-site", "Kültér", "Kültéri terület.")
        );
        final MetaTypeEntity indoorContainerType = type(
                metaData("fa-building", "office-site", "Beltér", "Beltéri terület.")
        );

        this.metaDataDao.save(testType);
        this.metaDataDao.save(indoorLuminaireType);
        this.metaDataDao.save(outLuminaireType);
        this.metaDataDao.save(faType);
        this.metaDataDao.save(outdoorContainerType);
        this.metaDataDao.save(indoorContainerType);

        final AssetContainerEntity testContainer = createContainer(TEST_CONTAINER_0, "Teszt konténer", testType2,
                createExtractor(TEST_EXTRACTOR_0, null, "Asset #0", testType, 47.48841662454038, 18.95464564781571),
                createExtractor(TEST_EXTRACTOR_1, null, "Asset #1", testType, 47.487474687444866, 18.95462403802867),
                createExtractor(TEST_EXTRACTOR_2, null, "Asset #2", testType, 47.488934831756886, 18.954812588955065),
                createExtractor(TEST_EXTRACTOR_3, null, "Asset #3", testType, 47.487955249555355, 18.954611999999997));
        final AssetContainerEntity kfki = createContainer("Kfki", outdoorContainerType,
                createExtractor(null, "Predixberry #0", faType, 0.0, 0.0));
        final AssetContainerEntity iel;
        final AssetContainerEntity iw;
        final AssetContainerEntity ge
                = createContainer("GE Lighting", outdoorContainerType,
                        iel = createContainer("Innovation Electronical Laboratory", indoorContainerType,
                                createExtractor(null, "Luminaire #0", indoorLuminaireType, 47.48841662454038, 18.95464564781571),
                                createExtractor(null, "Luminaire #1", indoorLuminaireType, 47.487474687444866, 18.95462403802867),
                                createExtractor(null, "Luminaire #2", indoorLuminaireType, 47.488934831756886, 18.954812588955065),
                                createExtractor(null, "Luminaire #3", indoorLuminaireType, 47.487955249555355, 18.954611999999997)),
                        iw = createContainer("Innovation Warehouse", indoorContainerType,
                                createExtractor(null, "Raspberry PI A", faType, 47.48841662454037, 18.95464564781570),
                                createExtractor(null, "Raspberry PI B", faType, 47.48841662454037, 18.95464564781570),
                                createExtractor(null, "Raspberry PI 2", faType, 47.48841662454037, 18.95464564781570),
                                createExtractor(null, "Odroid", faType, 47.48841662454037, 18.95464564781570),
                                createExtractor(null, "Recopro", faType, 47.48841662454037, 18.95464564781570),
                                createExtractor(null, "Snapdragon", faType, 47.48841662454037, 18.95464564781570)));
        setBounds(testContainer, new Coordinate(47.48841662454038, 18.95464564781571),
                new Coordinate(47.487474687444866, 18.95462403802867),
                new Coordinate(7.488934831756886, 18.954812588955065),
                new Coordinate(47.487955249555355, 18.954611999999997),
                new Coordinate(47.48841662454038, 18.95464564781571));
        setBounds(ge, new Coordinate(47.48841662454038, 18.95464564781571),
                new Coordinate(47.487474687444866, 18.95462403802867),
                new Coordinate(7.488934831756886, 18.954812588955065),
                new Coordinate(47.487955249555355, 18.954611999999997),
                new Coordinate(47.48841662454038, 18.95464564781571));
        setBounds(iw, new Coordinate(47.48841662454038, 18.95464564781571),
                new Coordinate(47.487474687444866, 18.95462403802867),
                new Coordinate(7.488934831756886, 18.954812588955065),
                new Coordinate(47.487955249555355, 18.954611999999997),
                new Coordinate(47.48841662454038, 18.95464564781571));
        setBounds(iel, new Coordinate(47.48841662454038, 18.95464564781571),
                new Coordinate(47.487474687444866, 18.95462403802867),
                new Coordinate(7.488934831756886, 18.954812588955065),
                new Coordinate(47.487955249555355, 18.954611999999997),
                new Coordinate(47.48841662454038, 18.95464564781571));

        final AssetContainerEntity uranos = createContainer("Hegyvidék", outdoorContainerType,
                createExtractor(null, "Hencz szerszáma", outLuminaireType, 47.48841662454038, 18.95464564781571));
    }
    static final String KEY_STORAGEUSAGE = "storage-usage";
    static final String KEY_MEMORYUSAGE = "memory-usage";
    static final String KEY_PROCESSORUSAGE = "processor-usage";
    static final String KEY_TEMPERATURE = "temperature";
    static final String KEY_HUMIDITY = "humidity";
    static final String KEY_NOISELEVEL = "noise-level";
    static final String KEY_CO = "co";
    static final String KEY_HB = "heartbeat";
    static final String KEY_TEST = "db-test";

}
