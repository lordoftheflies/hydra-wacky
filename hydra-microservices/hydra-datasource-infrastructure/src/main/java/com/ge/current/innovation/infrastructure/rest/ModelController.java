/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.infrastructure.rest;

import com.ge.current.innovation.storage.jpa.dal.AttributeRepository;
import com.ge.current.innovation.storage.jpa.dal.ClassificationRepository;
import com.ge.current.innovation.storage.jpa.entities.AttributeEntity;
import com.ge.current.innovation.storage.jpa.entities.ClassificationEntity;
import com.ge.current.innovation.infrastructure.dto.AttributeDto;
import com.ge.current.innovation.infrastructure.dto.ClassificationDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Backend API for modeling assets.
 *
 * @author Hegedűs László (212429780)
 */
@Api(value = "ModelController")
@RestController
@RequestMapping("/model")
public class ModelController {

    @Autowired
    private ClassificationRepository classificationRepository;
    @Autowired
    private AttributeRepository attributeRepository;

    @CrossOrigin
    @RequestMapping(
            value = "/classification",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/classification", nickname = "Classifications", notes = "Asset types.")
    public List<ClassificationDto> classifications() {
        return classificationRepository.findAll().stream().map((ClassificationEntity ce)
                -> new ClassificationDto(
                        ce.getId(),
                        ce.getFriendlyName(),
                        ce.getDescription(),
                        attributeRepository.findByClassification(ce.getId()).stream()
                        .map((AttributeEntity ae) -> new AttributeDto(
                                ae.getId(),
                                ae.getUri(),
                                ae.getFriendlyName(),
                                ae.getDescription(),
                                ae.getMeter().getUom()
                        )).collect(Collectors.toList())
                )).collect(Collectors.toList());
    }

    @CrossOrigin
    @RequestMapping(
            value = "/classification/{id}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/classification/{id}", nickname = "Classifications", notes = "Asset types.")
    public ClassificationDto classification(@PathVariable("id") Long id) {
        ClassificationEntity ce = classificationRepository.findOne(id);
        ClassificationDto dto = new ClassificationDto(
                ce.getId(),
                ce.getFriendlyName(),
                ce.getDescription(),
                attributeRepository.findByClassification(ce.getId()).stream().map((AttributeEntity ae) -> new AttributeDto(
                ae.getId(),
                ae.getUri(),
                ae.getFriendlyName(),
                ae.getDescription(),
                ae.getMeter().getUom()
        )).collect(Collectors.toList()));
        return dto;
    }

    @CrossOrigin
    @RequestMapping(
            value = "/attribute",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/attribute", nickname = "Attributes", notes = "Asset attribute types.")
    public List<AttributeDto> attributes() {
        return attributeRepository.findAll().stream()
                .map((AttributeEntity ae) -> new AttributeDto(
                        ae.getId(),
                        ae.getUri(),
                        ae.getFriendlyName(),
                        ae.getDescription(),
                        ae.getMeter().getUom()))
                .collect(Collectors.toList());
    }

    @CrossOrigin
    @RequestMapping(
            value = "/attribute/{id}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/attribute/{id}", nickname = "Attribute details", notes = "Asset attribute types details.")
    public AttributeDto attribute(@PathVariable(value = "id") Long id) {
        AttributeEntity ae = attributeRepository.findOne(id);
        return new AttributeDto(
                ae.getId(),
                ae.getUri(),
                ae.getFriendlyName(),
                ae.getDescription(),
                ae.getMeter().getUom());
    }

    @CrossOrigin
    @RequestMapping(
            value = "/classification/{id}/attributes",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/classification/{id}/attributes", nickname = "Attributes of a classification", notes = "Asset attribute types of a classification.")
    public List<AttributeDto> attributesOfClassification(@PathVariable(value = "id") Long id) {
        return attributeRepository.findByClassification(id).stream()
                .map((AttributeEntity ae) -> new AttributeDto(
                        ae.getId(),
                        ae.getUri(),
                        ae.getFriendlyName(),
                        ae.getDescription(),
                        ae.getMeter().getUom()))
                .collect(Collectors.toList());
    }

    @CrossOrigin
    @RequestMapping(
            value = "/classification/new",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    @ApiOperation(value = "/classification/new", nickname = "New classification", notes = "Create new classification.")
    public void createClassification(ClassificationDto dto) {
//        MetaTypeEntity type = new MetaTypeEntity();
//        type.setId(dto.getId());
//        type.setMd(dto.getMeta());
//        modelStore.getMetaEntityDao().save(type);
//        dto.getAttributes().forEach(a -> {
//            MetaAttributeEntity attribute = new MetaAttributeEntity();
//            attribute.setId(a.getId());
//            MetricEntity metric = modelStore.getMetricDao().findOne(a.getKey());
//            if (metric == null) {
//                metric = new MetricEntity();
//                metric.setId(a.getKey());
//                metric.setDefaultUnitName(a.getUnit());
//                modelStore.getMetricDao().save(metric);
//            }
//            attribute.setMetric(metric);
//            attribute.setMd(a.getMeta());
//            attribute.setOwner(type);
//            modelStore.getMetaEntityDao().save(attribute);
//
//        });
    }
}
