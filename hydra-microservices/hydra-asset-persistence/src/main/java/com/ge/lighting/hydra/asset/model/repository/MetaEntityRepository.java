/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.asset.model.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ge.lighting.hydra.asset.MetaData;
import com.ge.lighting.hydra.asset.model.entity.MetaAttributeEntity;
import com.ge.lighting.hydra.asset.model.entity.MetaEntity;
import com.ge.lighting.hydra.asset.model.entity.MetaTypeEntity;
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
public interface MetaEntityRepository extends PagingAndSortingRepository<MetaEntity, UUID> {

    @Override
    MetaEntity findOne(UUID id);
    
    @Query(value = "SELECT e FROM MetaTypeEntity e WHERE e.id = :id")
    MetaTypeEntity findClassification(@Param("id") UUID id);
    
    @Query(value = "SELECT e FROM MetaTypeEntity e")
    Iterable<MetaTypeEntity> findClassifications();

    @Query(value = "SELECT e FROM MetaAttributeEntity e WHERE e.id = :id")
    MetaAttributeEntity findAttribute(@Param("id") UUID id);

    @Query(value = "SELECT e FROM MetaAttributeEntity e")
    Iterable<MetaAttributeEntity> findAttributes();
    
    @Query(value = "SELECT e FROM MetaAttributeEntity e WHERE e.owner.id = :id")
    Iterable<MetaAttributeEntity> findAttributesOf(@Param("id") UUID id);

    @Query(value = "SELECT e.md FROM MetaEntity e WHERE e.id = :id")
    MetaData findMetaData(@Param("id") String id);

    @Query(value = "SELECT e FROM MetaEntity e WHERE e.id = :id")
    Iterable<MetaEntity> findContent(@Param("id") String id);
}
