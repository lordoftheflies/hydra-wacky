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
package com.ge.lighting.innovation.persistence.ext.hibernate.json;

import java.sql.Types;
import org.hibernate.spatial.dialect.postgis.PostgisDialect;
/**
 * Hibernate dialect with JSON and GIS support. 
 * Based on PostgreSQL 9.2 dialect.
 * @author Hegedűs László (212429780)
 */
public class JsonPostgisDialect extends PostgisDialect {

    public JsonPostgisDialect() {
        super();
        this.registerColumnType(Types.JAVA_OBJECT, "json");
    }

}
