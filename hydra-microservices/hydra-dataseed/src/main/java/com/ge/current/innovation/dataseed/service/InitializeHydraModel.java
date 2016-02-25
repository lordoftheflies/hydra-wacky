/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.dataseed.service;

import com.ge.current.innovation.hydra.storage.dal.AssetMeterRepository;
import com.ge.current.innovation.hydra.storage.dal.AssetRepository;
import com.ge.current.innovation.hydra.storage.dal.AttributeRepository;
import com.ge.current.innovation.hydra.storage.dal.ClassificationRepository;
import com.ge.current.innovation.hydra.storage.dal.MeterRepository;
import com.ge.current.innovation.hydra.storage.entities.AssetEntity;
import com.ge.current.innovation.hydra.storage.entities.AssetMeterEntity;
import com.ge.current.innovation.hydra.storage.entities.AttributeEntity;
import com.ge.current.innovation.hydra.storage.entities.ClassificationEntity;
import com.ge.current.innovation.hydra.storage.entities.MeterEntity;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 *
 * @author lordoftheflies
 */
@Service
@Profile("local")
public class InitializeHydraModel {

    private static final Logger LOG = Logger.getLogger(InitializeHydraModel.class.getName());

    @Autowired
    private MeterRepository meterRepository;
    @Autowired
    private ClassificationRepository classificationRepository;
    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private AssetMeterRepository assetMeterRepository;

    private AttributeEntity addAttribute(ClassificationEntity ce, AttributeEntity ae, MeterEntity me) {
        ae.setClassification(ce);
        ae.setMeter(me);
        return attributeRepository.save(ae);
    }

    private Iterable<AssetMeterEntity> instantiate(AssetEntity asset, ClassificationEntity classification) {
        return assetMeterRepository.save(
                attributeRepository.findByClassification(classification.getId()).stream()
                .map(a -> {
                    AssetMeterEntity ame = new AssetMeterEntity("/assetmeter/" + Math.abs(a.getFriendlyName().hashCode()), a.getFriendlyName(), a.getDescription());
                    ame.setAttribute(a);
                    ame.setAsset(asset);
                    return ame;
                })
                .collect(Collectors.toList()));
    }

    @PostConstruct

    public void initialize() {
        LOG.info("=============================================================================");
        LOG.info("SETUP DATAMODEL");
        LOG.info("=============================================================================");
        
        
        LOG.info("=============================================================================");
        LOG.info("CREATE ASSET-MODEL");
        LOG.info("=============================================================================");
        ClassificationEntity smartLuminaireClassification = classificationRepository.save(new ClassificationEntity("/classification/smart-luminaire", "Smart-luminaire", "Indoor smart-luminaire with sensors."));

        MeterEntity temperatureMeterEntity = meterRepository.save(new MeterEntity(null, "Temperature", "C°", null));
        MeterEntity lightLevelMeterEntity = meterRepository.save(new MeterEntity(null, "Light level", "C°", null));

        AttributeEntity indoorTemperatureAttribute = attributeRepository.save(new AttributeEntity("/attribute/temperature", "Temperature", "Indoor temperature."));
        AttributeEntity indoorLightLevelAttribute = attributeRepository.save(new AttributeEntity("/attribute/light-level", "Light level", "Indoor light-level."));

        indoorLightLevelAttribute = addAttribute(smartLuminaireClassification, indoorLightLevelAttribute, lightLevelMeterEntity);
        indoorTemperatureAttribute = addAttribute(smartLuminaireClassification, indoorTemperatureAttribute, temperatureMeterEntity);

        LOG.info("=============================================================================");
        LOG.info("INSTANTIATE ASSET-MODEL");
        LOG.info("=============================================================================");
        AssetEntity smartLuminaireAsset0 = assetRepository.save(new AssetEntity("/asset/luminaire0", "Smart-luminaire #0", "Smart-luminaire #0 description."));
        AssetEntity smartLuminaireAsset1 = assetRepository.save(new AssetEntity("/asset/luminaire1", "Smart-luminaire #1", "Smart-luminaire #1 description."));
        AssetEntity smartLuminaireAsset2 = assetRepository.save(new AssetEntity("/asset/luminaire2", "Smart-luminaire #2", "Smart-luminaire #2 description."));
        AssetEntity smartLuminaireAsset3 = assetRepository.save(new AssetEntity("/asset/luminaire3", "Smart-luminaire #3", "Smart-luminaire #3 description."));

        instantiate(smartLuminaireAsset0, smartLuminaireClassification);
        instantiate(smartLuminaireAsset1, smartLuminaireClassification);
        instantiate(smartLuminaireAsset2, smartLuminaireClassification);
        instantiate(smartLuminaireAsset3, smartLuminaireClassification);
        
        LOG.info("=============================================================================");
        LOG.info("READY TO USE");
        LOG.info("=============================================================================");
        
    }
}
