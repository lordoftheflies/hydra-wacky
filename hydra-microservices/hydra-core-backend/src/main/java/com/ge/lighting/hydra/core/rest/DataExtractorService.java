package com.ge.lighting.hydra.core.rest;

import com.ge.lighting.hydra.asset.DataExtractorDto;
import com.ge.lighting.hydra.asset.InfraDto;
import com.ge.lighting.hydra.asset.MetaData;
import com.ge.lighting.hydra.asset.MetaAttributeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ge.lighting.hydra.asset.device.entity.AssetContainerEntity;
import com.ge.lighting.hydra.asset.device.repository.AssetTreeRepository;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import com.ge.lighting.hydra.datastore.repository.DataRepository;
import com.ge.lighting.hydra.core.visitors.DetailsVisitor;
import com.ge.lighting.hydra.core.visitors.NavigationVisitor;
import com.ge.lighting.hydra.core.visitors.SpatialVisitor;
import com.ge.lighting.hydra.asset.model.repository.MetaEntityRepository;
import com.ge.lighting.hydra.asset.model.repository.MetricEntityRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;

@RestController
public class DataExtractorService {

    @Autowired
    private AssetTreeRepository assetTreeDao;

    @Autowired
    private NavigationVisitor navVisitor;
    @Autowired
    private SpatialVisitor spatialVisitor;
    @Autowired
    private DetailsVisitor detailsVisitor;

    @Autowired
    private MetricEntityRepository metricDao;

    @Autowired
    private MetaEntityRepository metaDataDao;

    @Autowired
    private DataRepository dataStoreDao;

    @RequestMapping(value = "location/roots", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public InfraDto roots() {
        InfraDto dto = new InfraDto();
        this.assetTreeDao.findRoots().forEach(entity -> {
            dto.getFeatures().add(spatialVisitor.visit(entity));
        });
        return dto;
    }

    @RequestMapping(value = "location/{assetId}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public InfraDto children(@PathVariable("assetId") String assetId) {
        InfraDto dto = new InfraDto();
        this.assetTreeDao.findLeafChildren(UUID.fromString(assetId)).forEach(entity -> {
            dto.getFeatures().add(spatialVisitor.visit(entity));
        });
        this.assetTreeDao.findForkChildren(UUID.fromString(assetId)).forEach(entity -> {
            dto.getFeatures().add(spatialVisitor.visit(entity));
        });
        return dto;
    }

    @RequestMapping(value = "asset/{assetId}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public DataExtractorDto details(@PathVariable("assetId") String assetId) {
        return detailsVisitor.visitBase(assetTreeDao.findOne(UUID.fromString(assetId)));
    }

    @RequestMapping(value = "metrics/{assetId}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<MetaAttributeDto> metrics(@PathVariable("assetId") String assetId) {
        List<MetaAttributeDto> metrics = new ArrayList<>();
        metaDataDao.findAttributesOf(assetTreeDao.findOne(UUID.fromString(assetId)).getMetaEntity().getId())
                .forEach(attribute -> {
                    final MetaAttributeDto dto = attribute.getMetric().getDto();
                    dto.setId(attribute.getId());
                    dto.setName(attribute.getMd().getLabel());
                    dto.setDescription(attribute.getMd().getDescription());
                    metrics.add(dto);
                });
        return metrics;
    }

    @RequestMapping(
            value = "navigation",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    @ResponseBody
    public MetaData navigation() {
        MetaData rootNode = new MetaData("fa-map-o", "site", "Site", "");
        final Iterable<AssetContainerEntity> findRoots = this.assetTreeDao.findRoots();
        if (findRoots != null) {
            findRoots.forEach(root -> {
                rootNode.getSubitems().add(navVisitor.visit(root));
            });
        }
        return rootNode;
    }
}
