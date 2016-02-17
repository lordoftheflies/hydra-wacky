/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.hydra.storage.message.dal;

import com.ge.current.innovation.hydra.storage.entities.AssetMeterEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author lordoftheflies
 */
public interface AssetMeterRepository extends CrudRepository<AssetMeterEntity, Long>{

    AssetMeterEntity findByUri(@Param("assetMeterUri") String assetMeterUri);
    boolean uriExists(@Param("assetMeterUri") String assetMeterUri);
    
}
