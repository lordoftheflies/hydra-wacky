/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.dataingestion.consumer.listeners;

import com.ge.current.innovation.DataPoint;
import com.ge.current.innovation.storage.jpa.dal.AssetMeterRepository;
import com.ge.current.innovation.storage.jpa.dal.DataPointRepository;
import com.ge.current.innovation.storage.jpa.entities.AssetMeterEntity;
import com.ge.current.innovation.storage.jpa.entities.DataPointEntity;
import com.ge.current.innovation.utils.JsonUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author lordoftheflies
 */
//@RabbitListener(queues = "data")
@Component
public class DataIngestionReceiver {

    private static final Logger LOG = Logger.getLogger(DataIngestionReceiver.class.getName());

    private SimpleDateFormat sdf = new SimpleDateFormat();

    @Autowired
    private DataPointRepository dataPointRepository;

    @Autowired
    private AssetMeterRepository assetMeterRepository;

    private AssetMeterEntity getAssetMeter(String assetMeterId) {
        if (assetMeterRepository.uriExists(assetMeterId)) {
            return assetMeterRepository.findByUri(assetMeterId);
        } else {
            LOG.log(Level.INFO, "Asset meter not exist, creating new one for {0}", assetMeterId);
            AssetMeterEntity entity = new AssetMeterEntity();
            entity.setUri(assetMeterId);
            return assetMeterRepository.save(entity);
        }
    }

    private DataPointEntity toEntity(DataPoint dto) throws ParseException {
        DataPointEntity entity = new DataPointEntity();
        if (dto.getTs() != null) entity.setTimeStamp(sdf.parse(dto.getTs()));
//        entity.setTimeStamp(sdf.parse(dto.getTs()));
        entity.setAssetMeter(getAssetMeter(dto.getCode()));
        entity.setVal(dto.getValue());
        return entity;
    }

//    @RabbitHandler
//    public void onMessage(Object msg) throws IOException, ParseException {
////    public void onMessage(String msg) throws IOException, ParseException {
////    public void handleMessage(String msg) throws ParseException {
////        LOG.log(Level.INFO, "Persist data-point: {0}", msg.toString());
//        LOG.log(Level.INFO, "Persist data-point: {0}", msg);
////        dataPointRepository.save(toEntity(msg));
//    }
//    @RabbitHandler
//    public void onMessage(DataPoint msg) throws IOException, ParseException {
    public void onMessage(byte[] msg) throws IOException, ParseException {
//    public void onMessage(String msg) throws IOException, ParseException {
//    public void handleMessage(String msg) throws ParseException {
//        LOG.log(Level.INFO, "Persist data-point: {0}", msg.toString());
        String str = new String(msg, StandardCharsets.UTF_8);
        DataPoint dp = new JsonUtils().read(DataPoint.class, str);
        LOG.log(Level.INFO, "Persist data-point: {0}", str);
        dataPointRepository.save(toEntity(dp));
    }

}
