/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra;

import com.ge.lighting.hydra.asset.device.entity.AssetEntity;

/**
 *
 * @author predix
 */
public class Naming {

    public static String friendlyNameFactory(AssetEntity asset) {
        return asset.getMetaEntity().getMd().getLabel() + asset.getId().hashCode();
    }

}
