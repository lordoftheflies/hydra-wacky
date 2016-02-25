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
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author lordoftheflies
 */
@Service
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

    @PostConstruct
    public void initialize() {
        ClassificationEntity smartLuminaireClassification = classificationRepository.save(new ClassificationEntity("/classification/smart-luminaire", "Smart-luminaire", "Indoor smart-luminaire with sensors."));
        AssetEntity smartLuminaireAsset0 = assetRepository.save(new AssetEntity());

        AttributeEntity indoorTemperatureAttribute = attributeRepository.save(new AttributeEntity("/attribute/temperature", "Temperature", "Indoor temperature."));
        MeterEntity temperatureMeterEntity = meterRepository.save(new MeterEntity(null, "Temperature", "C°", null));

        AttributeEntity indoorLightLevelAttribute = attributeRepository.save(new AttributeEntity("/attribute/light-level", "Light level", "Indoor light-level."));
        MeterEntity lightLevelMeterEntity = meterRepository.save(new MeterEntity(null, "Light level", "C°", null));

        addAttribute(smartLuminaireClassification, indoorLightLevelAttribute, lightLevelMeterEntity);
        addAttribute(smartLuminaireClassification, indoorTemperatureAttribute, temperatureMeterEntity);
    }
}
