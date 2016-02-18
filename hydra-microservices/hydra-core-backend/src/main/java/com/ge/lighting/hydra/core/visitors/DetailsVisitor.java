/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.core.visitors;

import com.ge.lighting.hydra.Visitor;
import com.ge.lighting.hydra.asset.DataExtractorDto;
import com.ge.lighting.hydra.asset.SensorDataDto;
import com.ge.lighting.hydra.asset.device.entity.AssetContainerEntity;
import com.ge.lighting.hydra.asset.device.entity.AssetEntity;
import com.ge.lighting.hydra.asset.device.entity.AssetExtractorEntity;
import com.ge.lighting.hydra.datastore.entity.DataPointEntity;
import com.ge.lighting.hydra.asset.device.repository.AssetTreeRepository;
import com.ge.lighting.hydra.datastore.repository.DataRepository;
import com.ge.lighting.hydra.asset.model.repository.MetaEntityRepository;
import com.ge.lighting.hydra.asset.model.repository.MetricEntityRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 *
 * @author predix
 */
@Component
public class DetailsVisitor implements Visitor<DataExtractorDto> {

    @Autowired
    private AssetTreeRepository assetDao;

    @Autowired
    private MetricEntityRepository metricDao;

    @Autowired
    private MetaEntityRepository metaRepo;

    @Autowired
    private DataRepository dataDao;

    @Override
    public DataExtractorDto visit(AssetExtractorEntity o) {
        DataExtractorDto dto = this.visitBase(o);
        dto.setSensors(new ArrayList<>());
        PageRequest p = new PageRequest(0, 1);
        metricDao.findAll().forEach(metric -> {
            Page<DataPointEntity> q = dataDao.findSet(o.getId(), Arrays.asList(new String[]{metric.getId()}), p);
            if (q.getTotalElements() > 0) {
                dto.getSensors().add(new SensorDataDto(metric.getDto(), q.getContent().get(0).getVal()));
            }
        });
        return dto;
    }

    public DataExtractorDto visitBase(AssetEntity o) {
        DataExtractorDto dto = new DataExtractorDto();
        dto.setId(o.getId().toString());
        dto.setClassification(o.getMetaEntity().getId());
        dto.setMetadata(o.getMetaEntity().getMd());
        dto.setSensors(new ArrayList<>());
        PageRequest p = new PageRequest(0, 1);
        metaRepo.findAttributesOf(o.getMetaEntity().getId()).forEach(attribute -> {
            Page<DataPointEntity> q = dataDao.findSet(o.getId(), Arrays.asList(new String[]{attribute.getMetric().getId()}), p);
            if (q.getTotalElements() > 0) {
                final SensorDataDto sensorDataDto = new SensorDataDto();
                sensorDataDto.setId(attribute.getId());
                sensorDataDto.setValue(q.getContent().get(0).getVal());
                sensorDataDto.setUnit(attribute.getMetric().getDefaultUnitName());
                sensorDataDto.setName(attribute.getMd().getLabel());
                sensorDataDto.setDescription(attribute.getMd().getDescription());
                dto.getSensors().add(sensorDataDto);
            }
        });
        return dto;
    }

    @Override
    public DataExtractorDto visit(AssetContainerEntity o) {
        DataExtractorDto dto = this.visitBase(o);

        return dto;
    }

}
