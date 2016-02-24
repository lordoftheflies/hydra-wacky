/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.hydra.storage.dal;

import com.ge.current.innovation.hydra.storage.entities.MeterEntity;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author lordoftheflies
 */
public interface MeterRepository extends CrudRepository<MeterEntity, Long> {
    
}
