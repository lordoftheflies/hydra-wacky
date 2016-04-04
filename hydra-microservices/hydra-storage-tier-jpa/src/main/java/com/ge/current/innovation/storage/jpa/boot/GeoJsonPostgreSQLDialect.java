/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.storage.jpa.boot;

import java.sql.Types;
import org.hibernate.dialect.PostgreSQL9Dialect;

/**
 *
 * @author lordoftheflies
 */
public class GeoJsonPostgreSQLDialect extends PostgreSQL9Dialect {

    public GeoJsonPostgreSQLDialect() {

        super();

        this.registerColumnType(Types.JAVA_OBJECT, "json");
    }
}
