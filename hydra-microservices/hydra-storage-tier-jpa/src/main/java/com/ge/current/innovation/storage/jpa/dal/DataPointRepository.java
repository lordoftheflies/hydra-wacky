/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.storage.jpa.dal;

import com.ge.current.innovation.storage.jpa.entities.DataPointEntity;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author lordoftheflies
 */
@Repository
public interface DataPointRepository extends PagingAndSortingRepository<DataPointEntity, Long> {

    @Override
    List<DataPointEntity> findAll();
    
    @Override
    List<DataPointEntity> findAll(Sort sort);

    @Override
    Page<DataPointEntity> findAll(Pageable pgbl);

    @Override
    List<DataPointEntity> findAll(Iterable<Long> itrbl);
    
    List<DataPointEntity> findSeriesFrom(
            @Param("key") String key, 
            @Param("begin") Date begin);
    
    List<DataPointEntity> findSeriesFromLocation(
            @Param("locationId") Long locationId, 
            @Param("key") String key, 
            @Param("begin") Date begin);
    
    List<DataPointEntity> findSeries(
            @Param("locationId") Long locationId, 
            @Param("key") Long key,
            @Param("begin") Date begin, 
            @Param("end") Date end);
    
    List<DataPointEntity> findSeriesEverywhere(
            @Param("key") Long key, 
            @Param("begin") Date begin, 
            @Param("end") Date end);
    
    @Override
    DataPointEntity findOne(Long id);
}
