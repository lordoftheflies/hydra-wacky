/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.visualization.rest;

import com.ge.current.innovation.visualization.dto.DataPoint;
import com.ge.current.innovation.visualization.dto.VisualizationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author predix
 */
@Api(value = "Dataset visualization")
@RestController
@RequestMapping("/set")
public class DataSetVisualizationController {
    private static final Logger LOG = Logger.getLogger(DataSetVisualizationController.class.getName());
    
    private static final SimpleDateFormat df = new SimpleDateFormat();
    
//    @Autowired
//    private DataStore store;

    @RequestMapping(
            value = "/realtime",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/set/realtime", nickname = "Current values", notes = "Last arrived sensor data.")
    public List<DataPoint> realtime() {
        List<DataPoint> dto = new ArrayList<>();
//        store.getDataStoreDao().findAll(new PageRequest(0, 100, new Sort(Sort.Direction.DESC, "id.ts"))).forEach(a -> dto.add(new DataPoint(
//                a.getId().getxType(), 
//                a.getId().getTs(), 
//                a.getId().getAsset(), 
//                a.getVal()
//        )));
        return dto;
    }
    
    @RequestMapping(
            value = "/{assetId}/{dimensionKey}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/", nickname = "Retrieve data", notes = "Shows a sensor data of a data-collector.")
    public List<DataPoint> leafData(
            @PathVariable("assetId") String assetId,
            @PathVariable("dimensionKey") String dimensionKey,
            @RequestParam(value = "begin", required = true) String begin,
            @RequestParam(value = "end", required = true) String end,
            @RequestParam(value = "index", required = false, defaultValue = "0") int index,
            @RequestParam(value = "size", required = false, defaultValue = "100") int size
    ) throws VisualizationException {
//        try {
            List<DataPoint> dto = new ArrayList<>();
//            store.getDataStoreDao().findSerie(
//                    UUID.fromString(assetId),
//                    dimensionKey,
//                    df.parse(begin),
//                    df.parse(end),
//                    new PageRequest(index, size, new Sort(Sort.Direction.DESC, "id.ts"))
//            ).forEach(
//                    a -> dto.add(new DataPoint(a.getId().getxType(), a.getId().getTs(), a.getId().getAsset(), a.getVal()))
//            );
            return dto;
//        } catch (ParseException ex) {
//            LOG.log(Level.SEVERE, EX_INPUT_PARSE_MSG, ex);
//            throw new VisualizationInputException(EX_INPUT_PARSE_MSG, ex);
//        }
    }
    
    static final String EX_INPUT_PARSE_MSG = "Could not parse input.";
}
