/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.storage.jpa.dal;

import com.ge.current.innovation.storage.jpa.entities.AssetEntity;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author lordoftheflies
 */
@Repository
public interface AssetRepository extends CrudRepository<AssetEntity, Long> {
    
    List<AssetEntity> findRoots();
    
    List<AssetEntity> findChildren(@Param("parentId") Long parentId);
}
