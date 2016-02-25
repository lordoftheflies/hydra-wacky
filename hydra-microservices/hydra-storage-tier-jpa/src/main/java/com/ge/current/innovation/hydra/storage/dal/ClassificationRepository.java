/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.hydra.storage.dal;

import com.ge.current.innovation.hydra.storage.entities.ClassificationEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author lordoftheflies
 */
@Repository
public interface ClassificationRepository extends PagingAndSortingRepository<ClassificationEntity, Long> {

    @Override
    Page<ClassificationEntity> findAll(Pageable pgbl);

    @Override
    List<ClassificationEntity> findAll(Sort sort);

    @Override
    List<ClassificationEntity> findAll(Iterable<Long> itrbl);

    @Override
    List<ClassificationEntity> findAll();
    
}
