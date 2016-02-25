/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.infrastructure.rest;

import com.ge.current.innovation.storage.jpa.dal.AssetRepository;
import com.ge.current.innovation.infrastructure.dto.AssetNodeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author predix
 */
@Api(value = "AssetTreeController")
@RestController
@RequestMapping("/assettree")
public class AssetTreeController {

    @Autowired
    private AssetRepository assetRepo;

    @CrossOrigin
    @RequestMapping(
            value = "/roots",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/roots", nickname = "Root assets.", notes = "Root assets.")
    public List<AssetNodeDto> roots() {
        List<AssetNodeDto> dto = new ArrayList<>();
        assetRepo.findRoots().forEach(a
                -> dto.add(new AssetNodeDto(
                        a.getId(),
                        null,
                        a.getClassification().getId(),
                        a.getFriendlyName(),
                        a.getDescription()
                )));
        return dto;
    }

    @CrossOrigin
    @RequestMapping(
            value = "/children/{id}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/children/{id}", nickname = "Children assets.", notes = "Children assets of the parent..")
    public List<AssetNodeDto> children(
            @PathVariable("id") Long id
    ) {
        List<AssetNodeDto> dto = new ArrayList<>();
        assetRepo.findChildren(id).forEach(a
                -> dto.add(new AssetNodeDto(a.getId(),
                        id,
                        a.getClassification().getId(),
                        a.getFriendlyName(),
                        a.getDescription()
                )));
        return dto;
    }

}
