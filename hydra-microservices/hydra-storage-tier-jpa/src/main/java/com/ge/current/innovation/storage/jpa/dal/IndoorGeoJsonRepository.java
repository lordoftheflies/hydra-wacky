/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.storage.jpa.dal;

import com.ge.current.innovation.storage.jpa.entities.IndoorEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author lordoftheflies
 */
@Repository
public interface IndoorGeoJsonRepository extends PagingAndSortingRepository<IndoorEntity, Long> {
    IndoorEntity findByStructureId(@Param("structureId") String structureId);
}
