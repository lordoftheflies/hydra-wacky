/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.hydra.datasource;

import com.ge.current.innovation.DataFilter;
import com.ge.current.innovation.StorageTierInfoDto;
import com.ge.current.innovation.HeatMapDto;
import com.ge.current.innovation.KpiDto;
import com.ge.current.innovation.SerieDto;
import com.ge.current.innovation.hydra.storage.entities.DataPointEntity;
import com.ge.current.innovation.hydra.storage.message.dal.DataPointRepository;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lordoftheflies
 */
@RestController
@RequestMapping(
        path = "/visialization", 
        consumes = MediaType.APPLICATION_JSON_VALUE, 
        produces = MediaType.APPLICATION_JSON_VALUE)
public class VisualizationController {

    private static final Logger LOG = Logger.getLogger(VisualizationController.class.getName());

    @Autowired
    private DataPointRepository dataPointRepo;

    @RequestMapping(method = RequestMethod.GET)
    public StorageTierInfoDto info(@RequestBody DataFilter filter) {
        LOG.log(Level.INFO, "Storage tier info ...");
        return new StorageTierInfoDto();
    }
    
    @RequestMapping(
            path = "/set",
            method = RequestMethod.POST)
    public List<KpiDto> dataSet(@RequestBody DataFilter filter) {
        LOG.log(Level.INFO, "Visualize data (filter: {0}) ...", filter);
        List<KpiDto> dto = dataPointRepo.findAll().stream()
                .map((DataPointEntity entity) -> new KpiDto("assetMeterUri", "name", entity.getTimeStamp(), entity.getVal()))
                .collect(Collectors.toList());
        LOG.log(Level.INFO, "Result: {0}", filter);
        return dto;
    }

    
    @RequestMapping(
            path = "/heatmap",
            method = RequestMethod.POST)
    public HeatMapDto heatMap(@RequestBody DataFilter filter) {
        LOG.log(Level.INFO, "Visualize data (filter: {0}) ...", filter);
        HeatMapDto dto = new HeatMapDto();
        dto.setPoints(dataPointRepo.findAll().stream()
                .map((DataPointEntity entity) -> new Double[]{0.0, 0.0, entity.getVal()})
                .collect(Collectors.toList()));
        LOG.log(Level.INFO, "Result: {0}", filter);
        return dto;
    }

    @RequestMapping(
            path = "/serie",
            method = RequestMethod.POST)
    public SerieDto seriesDto(@RequestBody DataFilter filter) {
        LOG.log(Level.INFO, "Visualize data (filter: {0}) ...", filter);
        SerieDto dto = new SerieDto();
        LOG.log(Level.INFO, "Result: {0}", filter);
        return dto;
    }
}
