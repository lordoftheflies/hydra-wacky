/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.hydra.storage.dal;

import com.ge.current.innovation.hydra.storage.entities.AttributeEntity;
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
public interface AttributeRepository extends PagingAndSortingRepository<AttributeEntity, Long> {

    @Override
    List<AttributeEntity> findAll(Sort sort);

    @Override
    Page<AttributeEntity> findAll(Pageable pgbl);

    List<AttributeEntity> findByClassification(@Param("classificationId") Long classificationId);

    @Override
    List<AttributeEntity> findAll();

    @Override
    List<AttributeEntity> findAll(Iterable<Long> itrbl);

}
