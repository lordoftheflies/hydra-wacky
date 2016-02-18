/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.core.rest;

import com.ge.lighting.hydra.asset.DataFilter;
import com.ge.lighting.hydra.datastore.entity.DataPointEntity;
import com.ge.lighting.predix.visualization.TimeSeries;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import com.ge.lighting.hydra.datastore.repository.DataRepository;
import com.ge.lighting.hydra.m2m.common.DataPoint;
import com.ge.lighting.hydra.asset.model.entity.MetaAttributeEntity;
import com.ge.lighting.hydra.asset.model.entity.MetaEntity;
import com.ge.lighting.hydra.asset.model.entity.MetricEntity;
import com.ge.lighting.hydra.asset.model.repository.MetaEntityRepository;
import com.ge.lighting.hydra.asset.model.repository.MetricEntityRepository;
import java.util.Arrays;

/**
 *
 * @author predix
 */
@RestController
public class DataObserverService {

    private static final Logger LOG = Logger.getLogger(DataObserverService.class.getName());

    @Autowired
    private DataRepository dataStoreDao;
    @Autowired
    private MetricEntityRepository metricDao;
    @Autowired
    private MetaEntityRepository metaDataDao;

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");

    private DataPoint toDto(DataPointEntity entity){
        return new DataPoint(entity.getId().getxType(), entity.getId().getTs(), entity.getId().getAsset(), entity.getVal());
    }
    
    @RequestMapping(value = "/data/current", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public DataPoint currentValue(@RequestParam("boxId") String boxId, @RequestParam("key") String key) {
        return toDto(dataStoreDao.findSet(UUID.fromString(boxId), Arrays.asList(new String[]{key}), new PageRequest(0, 100))
                .getContent().get(0));
    }

    @RequestMapping(value = "/data/set", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<DataPoint> values(
            @RequestParam("boxId") String boxId,
            @RequestParam(value = "key") String key,
            @RequestParam(value = "ts") String from,
            @RequestParam(value = "te") String to,
            @RequestParam(value = "limit", defaultValue = "100") int limit,
            @RequestParam(value = "pages", defaultValue = "0") int page) {
        List<DataPoint> points = new ArrayList<>();
        dataStoreDao.findSet(UUID.fromString(boxId), Arrays.asList(new String[]{key}), new PageRequest(page, limit))
                .getContent().forEach(entity -> points.add(toDto(entity)));
        return points;
    }

    private static final String BEGIN = "begin";
    private static final String END = "end";

    @RequestMapping(value = "/data/timeserie", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public TimeSeries timeSeries(
            @RequestParam("boxId") String boxId,
            @RequestParam(value = "key") String key,
            @RequestParam(value = "ts", defaultValue = BEGIN) String from,
            @RequestParam(value = "te", defaultValue = END) String to,
            @RequestParam(value = "limit", defaultValue = "100") int limit,
            @RequestParam(value = "pages", defaultValue = "0") int page) throws QueryInputException {
        TimeSeries dto = new TimeSeries();
        MetricEntity m = metricDao.findOne(key);
        dto.newSerie("(" + m.getDefaultUnitName() + ")");

//        points.setMax(1.0);
//        points.setMin(0.0);
//        points.setMean(0.5);
        Date ts = null, te = null;
        try {
            Page<DataPointEntity> p = null;
            if (BEGIN.equals(from) && !END.equals(to)) {
                te = df.parse(to);
                p = dataStoreDao.findSerieFromBegin(UUID.fromString(boxId), key, te, new PageRequest(page, limit));
            } else if (!BEGIN.equals(from) && END.equals(to)) {
                ts = df.parse(from);
                p = dataStoreDao.findSerieToEnd(UUID.fromString(boxId), key, ts, new PageRequest(page, limit));
            } else if (!BEGIN.equals(from) && !END.equals(to)) {
                ts = df.parse(from);
                te = df.parse(to);
                p = dataStoreDao.findSerie(UUID.fromString(boxId), key, ts, te, new PageRequest(page, limit));
            } else {
//            } else if (!BEGIN.equals(from) && !END.equals(to)) {
                p = dataStoreDao.findSerie(UUID.fromString(boxId), key, new PageRequest(page, limit));
            }
            p.getContent().forEach(entity -> {
                dto.pushData(entity.getId().getTs(), 0, entity.getVal());
            });
            return dto;
        } catch (ParseException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new QueryInputException();
        }
    }

    @RequestMapping(value = "/data/set",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    @ResponseBody
    public List<DataPoint> dataSet(@RequestBody DataFilter filter) throws QueryInputException {
        List<DataPoint> points = new ArrayList<>();
        dataStoreDao.findSet(UUID.fromString(filter.getBoxId()), filter.getKeys(),
                new PageRequest(filter.getCurrentPage(), filter.getLimit()))
                .getContent().forEach(entity -> points.add(toDto(entity)));
        return points;
    }

    @RequestMapping(value = "/data/timeserie",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    @ResponseBody
    public TimeSeries timeSeries(@RequestBody DataFilter filter) throws QueryInputException {
        TimeSeries dto = new TimeSeries();
        final Page<DataPointEntity> p;
        filter.getKeys().forEach((String key) -> {
            MetaEntity m = metaDataDao.findOne(UUID.fromString(key));
            dto.newSerie(m.getMd().getLabel() + " (" + ((MetaAttributeEntity) m).getMetric().getDefaultUnitName() + ")");
        });

        try {
            p = dataStoreDao.findSerie(UUID.fromString(filter.getBoxId()),
                    filter.getKeys(),
                    df.parse(filter.getTs()),
                    df.parse(filter.getTe()),
                    new PageRequest(filter.getCurrentPage(), filter.getLimit()));
            p.getContent().forEach(entity -> {
                dto.pushData(entity.getId().getTs(), entity.getId().getxType(), entity.getVal());
            });
        } catch (ParseException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new QueryInputException();
        }

        return dto;

    }
}
