/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.asset.device.repository;

import com.ge.lighting.hydra.asset.device.entity.AssetContainerEntity;
import com.ge.lighting.hydra.asset.device.entity.AssetEntity;
import com.ge.lighting.hydra.asset.device.entity.AssetExtractorEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author predix
 */
@Repository
public interface AssetTreeRepository extends PagingAndSortingRepository<AssetEntity, UUID> {

    @Query(value = "SELECT a FROM AssetContainerEntity a WHERE a.parent = null")
    Iterable<AssetContainerEntity> findRoots();

    @Query(value = "SELECT a FROM AssetContainerEntity a WHERE a.parent.id = :id")
    Iterable<AssetContainerEntity> findForkChildren(@Param("id") UUID id);

    @Query(value = "SELECT a FROM AssetExtractorEntity a WHERE a.parent.id = :id")
    Iterable<AssetExtractorEntity> findLeafChildren(@Param("id") UUID id);

    @Query(value = "SELECT a FROM AssetEntity a WHERE a.parent.id = :id")
    Iterable<AssetEntity> findChildren(@Param("id") UUID id);

    
    @Query(value = "SELECT a FROM AssetContainerEntity a WHERE a.id = :id")
    AssetContainerEntity findFork(@Param("id") UUID id);

    @Query(value = "SELECT a FROM AssetContainerEntity a")
    Iterable<AssetContainerEntity> findForks();

    @Query(value = "SELECT a FROM AssetExtractorEntity a WHERE a.id = :id")
    AssetExtractorEntity findLeaf(@Param("id") UUID id);
    
    @Query(value = "SELECT a FROM AssetExtractorEntity a")
    Iterable<AssetExtractorEntity> findLeafs();
    
}
