package com.ge.lighting.hydra.asset.model.repository;

/**
 * Copyright (c) 2013 GE Global Research. All rights reserved.
 *
 * The copyright to the computer software herein is the property of GE Global
 * Research. The software may be used and/or copied only with the written
 * permission of GE Global Research or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the software has
 * been supplied.
 */
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.ge.lighting.hydra.asset.model.entity.EventEntity;

/**
 * This class models the spring-data repository for alarmevent entity. Apart
 * form the standard operations supported by CRUD Repository, this class also
 * supports customized named queries ,pagination, sorting and type safe queries
 * using query-dsl.
 *
 * @author Hegedűs László (212429780)
 */
@Repository
public interface IEventEntityRepository extends PagingAndSortingRepository<EventEntity, Long> {

    /**
     * Get all alarm events
     *
     * @return List of all events.
     */
    @Override
    List<EventEntity> findAll();
}
