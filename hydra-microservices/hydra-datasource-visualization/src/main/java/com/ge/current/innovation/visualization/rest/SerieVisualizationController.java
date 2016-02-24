/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.visualization.rest;

import com.ge.current.innovation.visualization.dto.SerieDto;
import com.ge.current.innovation.visualization.dto.VisualizationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
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
@Api(value = "Serie visualization")
@RestController
@RequestMapping("/serie")
public class SerieVisualizationController {

    private static final Logger LOG = Logger.getLogger(SerieVisualizationController.class.getName());

//    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
    private static final SimpleDateFormat df = new SimpleDateFormat();

//    @Autowired
//    private DataStore store;

    @RequestMapping(
            value = "/realtime",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/realtime", nickname = "Current values", notes = "This shows the last arrived sensor data serie.")
    public Map<String, SerieDto> realtime() {
        Map<String, SerieDto> dto = new HashMap<>();
        SerieDto serie0 = new SerieDto("serie0");
        dto.put(serie0.getName(), serie0);
//        store.getDataStoreDao().findAll(new PageRequest(0, 100, new Sort(Sort.Direction.DESC, "id.ts"))).forEach(a
//                -> serie0.add(new SeriePoint(a.getId().getTs(), a.getVal()))
//        );
        return dto;
    }

    @RequestMapping(
            value = "/{assetId}/{dimensionKey}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/", nickname = "Retrieve data", notes = "Shows a sensor data of a data-collector.")
    public SerieDto leafData(
            @PathVariable("assetId") String assetId,
            @PathVariable("dimensionKey") String dimensionKey,
            @RequestParam(value = "begin", required = true) String begin,
            @RequestParam(value = "end", required = true) String end,
            @RequestParam(value = "index", required = false, defaultValue = "0") int index,
            @RequestParam(value = "size", required = false, defaultValue = "100") int size
    ) throws VisualizationException {
//        try {
            SerieDto dto = new SerieDto(dimensionKey);
//            store.getDataStoreDao().findSerie(
//                    UUID.fromString(assetId),
//                    dimensionKey,
//                    df.parse(begin),
//                    df.parse(end),
//                    new PageRequest(index, size, new Sort(Sort.Direction.DESC, "id.ts"))
//            ).forEach(
//                    a -> dto.put(a.getId().getTs(), a.getVal())
//            );
            return dto;
//        } catch (ParseException ex) {
//            LOG.log(Level.SEVERE, EX_INPUT_PARSE_MSG, ex);
//            throw new VisualizationInputException(EX_INPUT_PARSE_MSG, ex);
//        }
    }
    static final String EX_INPUT_PARSE_MSG = "Could not parse input.";
}
