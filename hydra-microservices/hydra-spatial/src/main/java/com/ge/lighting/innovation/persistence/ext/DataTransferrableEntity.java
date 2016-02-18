/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.innovation.persistence.ext;

import java.io.Serializable;

/**
 * Data-transfer behavior for entity.
 * @author Hegedűs László (212429780)
 * @param <Dto> Data-tansfer-object type.
 */
public interface DataTransferrableEntity<Dto extends Serializable> {
    /**
     * Data-transfer-object factory method.
     * @return Data-tansfer-object from the object.
     */
    Dto getDto();
}
