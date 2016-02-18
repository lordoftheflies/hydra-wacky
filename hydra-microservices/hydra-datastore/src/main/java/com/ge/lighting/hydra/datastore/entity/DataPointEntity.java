/*
 * Copyright 2015 Hegedűs László (212429780).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ge.lighting.hydra.datastore.entity;

import javax.persistence.Entity;

/**
 *
 * @author Hegedűs László (212429780)
 */
@Entity
//@NamedQueries({
//    @NamedQuery(name = "SimpleDataEntry.findLastValues", query = "SELECT e FROM SimpleDataEntry e WHERE e.id.asset = :boxId AND e.id.xType = :key ORDER BY e.id.ts DESC")
//})
public class DataPointEntity extends DataEntryEntity<Double> {

    public DataPointEntity() {
        super();
        // TODO Auto-generated constructor stub
    }

    public DataPointEntity(DataKey id, Double val) {
        super(id, val);
        // TODO Auto-generated constructor stub
    }
}
