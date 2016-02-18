/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.core.visitors;

import com.ge.lighting.hydra.Visitor;
import com.ge.lighting.hydra.asset.AssetMetaData;
import com.ge.lighting.hydra.asset.device.entity.AssetContainerEntity;
import com.ge.lighting.hydra.asset.device.entity.AssetEntity;
import com.ge.lighting.hydra.asset.device.entity.AssetExtractorEntity;
import com.ge.lighting.hydra.asset.device.repository.AssetTreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author predix
 */
@Component
public class NavigationVisitor implements Visitor<AssetMetaData> {

    @Autowired
    private AssetTreeRepository assetDao;

    private AssetMetaData visitBase(AssetEntity asset) {
        AssetEntity e = assetDao.findOne(asset.getId());
        AssetMetaData result = new AssetMetaData();
        result.setDescription(e.getMetaEntity().getMd().getDescription());
        result.setIcon(e.getMetaEntity().getMd().getIcon());
        result.setLabel(e.getMetaEntity().getMd().getLabel());
        result.setState(e.getMetaEntity().getMd().getState());
        return result;
    }

    @Override
    public AssetMetaData visit(AssetContainerEntity asset) {
        AssetMetaData dto = this.visitBase(asset);
        final Iterable<AssetContainerEntity> findForkChildren = assetDao.findForkChildren(asset.getId());
        if (findForkChildren != null) {
            findForkChildren.forEach(child -> {
                dto.getSubitems().add(this.visit(child));
            });
        }
        final Iterable<AssetExtractorEntity> findLeafChildren = assetDao.findLeafChildren(asset.getId());
        if (findForkChildren != null) {
            findLeafChildren.forEach(child -> {
                if (child != null) {
                    dto.getSubitems().add(this.visit(child));
                }
            });
        }
        return dto;
    }

//    
    @Override
    public AssetMetaData visit(AssetExtractorEntity asset) {
        return this.visitBase(asset);
    }
}
