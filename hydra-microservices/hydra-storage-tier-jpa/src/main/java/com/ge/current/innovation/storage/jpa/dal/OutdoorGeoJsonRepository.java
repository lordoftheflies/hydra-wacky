/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.storage.jpa.dal;

import com.ge.current.innovation.storage.jpa.entities.IndoorEntity;
import com.ge.current.innovation.storage.jpa.entities.OutdoorEntity;
import com.ge.current.innovation.storage.jpa.entities.SiteEntity;
import java.io.Serializable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author lordoftheflies
 */
@Repository
public interface OutdoorGeoJsonRepository extends PagingAndSortingRepository<OutdoorEntity, Long> {
    OutdoorEntity findByStructureId(@Param("structureId") String structureId);
}
