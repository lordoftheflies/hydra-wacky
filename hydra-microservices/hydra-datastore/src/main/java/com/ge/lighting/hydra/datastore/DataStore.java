/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.datastore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import com.ge.lighting.hydra.datastore.repository.DataRepository;

/**
 *
 * @author Hegedűs László (212429780)
 */
@Component
@EnableAutoConfiguration
public class DataStore {
	static final String ENTITY_PACKAGE = "com.ge.lighting.hydra.datastore.entity";
    static final String DAL_PACKAGE = "com.ge.lighting.hydra.datastore.repository";

    @Autowired
    private DataRepository dataStoreDao;

    public DataRepository getDataStoreDao() {
        return dataStoreDao;
    }

    public void setDataStoreDao(DataRepository dataStoreDao) {
        this.dataStoreDao = dataStoreDao;
    }
}
