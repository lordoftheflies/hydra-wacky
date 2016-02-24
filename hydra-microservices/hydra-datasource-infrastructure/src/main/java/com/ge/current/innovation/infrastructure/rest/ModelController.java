/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.infrastructure.rest;

import com.ge.current.innovation.infrastructure.dto.AttributeDto;
import com.ge.current.innovation.infrastructure.dto.ClassificationDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
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

//    @Autowired
//    private ModelStore modelStore;
    @CrossOrigin
    @RequestMapping(
            value = "/classification",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/classification", nickname = "Classifications", notes = "Asset types.")
    public List<ClassificationDto> classifications() {
        List<ClassificationDto> dto = new ArrayList<>();
//        modelStore.getMetaEntityDao().findClassifications().forEach(a
//                -> dto.add(new ClassificationDto(
//                        a.getId(),
//                        a.getMd().getLabel(),
//                        a.getMd().getDescription()
//                )));
        return dto;
    }

    @CrossOrigin
    @RequestMapping(
            value = "/classification/{id}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/classification/{id}", nickname = "Classifications", notes = "Asset types.")
    public ClassificationDto classification(@PathVariable("id") String id) {
//        MetaEntÍ= modelStore.getMetaEntityDao().findOne(UUID.fromString(id));
//        ClassificationDto dto = new ClassificationDto(cf.getId(), cf.getMd().getLabel(), cf.getMd().getDescription());
        ClassificationDto dto = new ClassificationDto();
//        List<AttributeDto> attributes = new ArrayList<>();
//        modelStore.getMetaEntityDao().findAttributesOf(UUID.fromString(id)).forEach(a
//                -> attributes.add(new AttributeDto(a.getId(), 
//                        a.getMetric().getId(),
//                        a.getMd().getLabel(), 
//                        a.getMd().getDescription(), 
//                        a.getMetric().getDefaultUnitName())));
//        dto.setAttributes(attributes);
        return dto;
    }

    @CrossOrigin
    @RequestMapping(
            value = "/attribute",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/attribute", nickname = "Attributes", notes = "Asset attribute types.")
    public List<AttributeDto> attributes() {
        List<AttributeDto> dto = new ArrayList<>();
//        modelStore.getMetaEntityDao().findAttributes().forEach(a
//                -> dto.add(new AttributeDto(a.getId(), 
//                        a.getMetric().getId(), 
//                        a.getMd().getLabel(), 
//                        a.getMd().getDescription(), 
//                        a.getMetric().getDefaultUnitName())));
        return dto;
    }

    @CrossOrigin
    @RequestMapping(
            value = "/attribute/{id}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/attribute/{id}", nickname = "Attribute details", notes = "Asset attribute types details.")
    public AttributeDto attribute(@PathVariable(value = "id") String id) {
//        MetaAttributeEntity attribute = modelStore.getMetaEntityDao().findAttribute(UUID.fromString(id));
//        return new AttributeDto(attribute.getId(), 
//                attribute.getMetric().getId(), 
//                attribute.getMd().getLabel(), 
//                attribute.getMd().getDescription(), 
//                attribute.getMetric().getDefaultUnitName());
        return new AttributeDto();
    }

    @CrossOrigin
    @RequestMapping(
            value = "/classification/{id}/attributes",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "/classification/{id}/attributes", nickname = "Attributes of a classification", notes = "Asset attribute types of a classification.")
    public List<AttributeDto> attributesOfClassification(@PathVariable(value = "id") String id) {
        List<AttributeDto> dto = new ArrayList<>();
//        modelStore.getMetaEntityDao().findAttributesOf(UUID.fromString(id)).forEach(attribute
//                -> dto.add(new AttributeDto(attribute.getId(), attribute.getMetric().getId(), attribute.getMd().getLabel(), attribute.getMd().getDescription(), attribute.getMetric().getDefaultUnitName()))
//        );
        return dto;
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
