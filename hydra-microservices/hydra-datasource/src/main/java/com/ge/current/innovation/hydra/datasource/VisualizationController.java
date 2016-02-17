/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.hydra.datasource;

import com.ge.current.innovation.DataFilter;
import com.ge.current.innovation.HeatMapDto;
import com.ge.current.innovation.KpiDto;
import com.ge.current.innovation.SerieDto;
import com.ge.current.innovation.hydra.storage.entities.DataPointEntity;
import com.ge.current.innovation.hydra.storage.message.dal.DataPointRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lordoftheflies
 */
@RestController
@RequestMapping(path = "/visialization", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class VisualizationController {

    private static final Logger LOG = Logger.getLogger(VisualizationController.class.getName());

    @Autowired
    private DataPointRepository dataPointRepo;

    @RequestMapping(
            consumes = "/set",
            method = RequestMethod.GET)
    public List<KpiDto> dataSet(DataFilter filter) {
        LOG.log(Level.INFO, "Visualize data (filter: {0}) ...", filter);
        List<KpiDto> dto = dataPointRepo.findAll().stream()
                .map((DataPointEntity entity) -> new KpiDto("assetMeterUri", "name", entity.getTimeStamp(), entity.getVal()))
                .collect(Collectors.toList());
        LOG.log(Level.INFO, "Result: {0}", filter);
        return dto;
    }

    
    public HeatMapDto heatMap(DataFilter filter) {
        LOG.log(Level.INFO, "Visualize data (filter: {0}) ...", filter);
        HeatMapDto dto = new HeatMapDto();
        dto.setPoints(dataPointRepo.findAll().stream()
                .map((DataPointEntity entity) -> new Double[]{0.0, 0.0, entity.getVal()})
                .collect(Collectors.toList()));
        LOG.log(Level.INFO, "Result: {0}", filter);
        return dto;
    }

    public SerieDto seriesDto(DataFilter filter) {
        LOG.log(Level.INFO, "Visualize data (filter: {0}) ...", filter);
        SerieDto dto = new SerieDto();
        LOG.log(Level.INFO, "Result: {0}", filter);
        return dto;
    }
}
