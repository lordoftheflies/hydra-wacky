/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.dataingestion.boot;

import com.ge.current.innovation.DataPoint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lordoftheflies
 */
public class DataIngestionListener {

    private static final Logger LOG = Logger.getLogger(DataIngestionListener.class.getName());

    private SimpleDateFormat sdf = new SimpleDateFormat();

//    private DataPointRepository dataPointRepository;
//
//    private AssetMeterRepository assetMeterRepository;

//    public DataIngestionListener(DataPointRepository dataPointRepository, AssetMeterRepository assetMeterRepository) {
//        this.dataPointRepository = dataPointRepository;
//        this.assetMeterRepository = assetMeterRepository;
//    }

//    private AssetMeterEntity getAssetMeter(String assetMeterId) {
//        if (assetMeterRepository.uriExists(assetMeterId)) {
//            return assetMeterRepository.findByUri(assetMeterId);
//        } else {
//            LOG.log(Level.INFO, "Asset meter not exist, creating new one for {0}", assetMeterId);
//            AssetMeterEntity entity = new AssetMeterEntity();
//            entity.setUri(assetMeterId);
//            return assetMeterRepository.save(entity);
//        }
//    }
//
//    private DataPointEntity toEntity(DataPoint dto) throws ParseException {
//        DataPointEntity entity = new DataPointEntity();
//        entity.setTimeStamp(sdf.parse(dto.getTs()));
//        entity.setAssetMeter(getAssetMeter(dto.getCode()));
//        entity.setVal(dto.getValue());
//        return entity;
//    }

    public void onMessage(DataPoint msg) throws ParseException {
//    public void onMessage(String msg) throws ParseException {
        LOG.log(Level.INFO, "Ingest data-point: {0}", msg.toString());
//        dataPointRepository.save(toEntity(msg));
    }

}
