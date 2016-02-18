/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra;

import com.ge.lighting.hydra.asset.device.entity.AssetContainerEntity;
import com.ge.lighting.hydra.asset.device.entity.AssetExtractorEntity;

/**
 *
 * @author predix
 */
public interface Visitor<T> {

    T visit(AssetExtractorEntity o);

    T visit(AssetContainerEntity o);
}
