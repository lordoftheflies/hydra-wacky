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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;

/**
 * Json Hibernate usertype.
 *
 * @author Hegedűs László (212429780)
 */
public class PgJsonObject implements UserType {

    /**
     * Jackson object-mapper
     */
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
//        OBJECT_MAPPER.configure(Feature.IGNORE_UNKNOWN, false);
    }

    /**
     * Sql side of ORM.
     *
     * @return Array with Java object
     */
    @Override
    public int[] sqlTypes() {
        return new int[]{Types.JAVA_OBJECT};
    }

    /**
     * Java class of ORM
     *
     * @return This class.
     */
    @Override
    public Class returnedClass() {
        return this.getClass();
    }

    /**
     *
     * @param a First JSON
     * @param b Second JSON
     * @return Determine first and second JSON equality.
     * @throws HibernateException Invalid JSON mapping.
     */
    @Override
    public boolean equals(Object a, Object b) throws HibernateException {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    /**
     * Hash generator function.
     * @param o JSON object.
     * @return Generated hash.
     * @throws HibernateException Invalid JSON mapping.
     */
    @Override
    public int hashCode(Object o) throws HibernateException {
        return o.hashCode();
    }

    /**
     * Get object from row.
     * Nullsafe.
     * @param resultSet Resultset.
     * @param names Property names.
     * @param si Session implementor.
     * @param o JSON object.
     * @return Mapped JSON object.
     * @throws HibernateException Invalid JSON mapping.
     * @throws SQLException SQL level error.
     */
    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor si, Object o) throws HibernateException, SQLException {
        if (resultSet.getObject(names[0]) == null) {
            return null;
        }
        PGobject pGobject = (PGobject) resultSet.getObject(names[0]);
        Object jsonObject = null;
        try {
            jsonObject = OBJECT_MAPPER.readValue(pGobject.getValue(), this.returnedClass());
        } catch (IOException e) {
            throw new RuntimeException("Hibernate could not deserialize JSON.", e);
        }
        return jsonObject;
    }

    /**
     * Set JSON object.
     * @param preparedStatement Statement.
     * @param value JSON object.
     * @param index Index.
     * @param si Session implementor.
     * @throws HibernateException Invalid JSON mapping.
     * @throws SQLException SQL level error.
     */
    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SessionImplementor si) throws HibernateException, SQLException {
        if (value == null) {
            preparedStatement.setNull(index, Types.NULL);
            return;
        }
        String jsonString = null;
        try {
            jsonString = OBJECT_MAPPER.writeValueAsString(value);
        } catch (IOException e) {
            throw new RuntimeException("Hibernate could not serialize JSON.", e);
        }

        PGobject pGobject = new PGobject();
        pGobject.setType("json");
        pGobject.setValue(jsonString);
        preparedStatement.setObject(index, pGobject);
    }

    /**
     * Deep copy.
     * @param o Source JSON object.
     * @return Deep copy of JSON object.
     * @throws HibernateException Invalid JSON mapping.
     */
    @Override
    public Object deepCopy(Object o) throws HibernateException {
        Object copy = null;
        try {
            copy = OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsBytes(o), this.returnedClass());
        } catch (IOException e) {
            throw new RuntimeException("Hibernate could deep copy JSON.", e);
        }
        return copy;
    }

    /**
     * Content is mutable.
     * @return True if the content is mutable.
     */
    @Override
    public boolean isMutable() {
        return true;
    }

    /**
     * Disassemble.
     * @param o JSON object.
     * @return Serialized object.
     * @throws HibernateException Invalid JSON mapping.
     */
    @Override
    public Serializable disassemble(Object o) throws HibernateException {
        return (Serializable) this.deepCopy(o);
    }

    /**
     * Assemble.
     * @param serializable JSON object.
     * @param o Object.
     * @return Mapped object.
     * @throws HibernateException Invalid JSON mapping.
     */
    @Override
    public Object assemble(Serializable serializable, Object o) throws HibernateException {
        return this.deepCopy(serializable);
    }

    /**
     * Replace object.
     * @param o Source
     * @param o1 Dest
     * @param o2 ???
     * @return destintation object.
     * @throws HibernateException Invalid JSON mapping.
     */
    @Override
    public Object replace(Object o, Object o1, Object o2) throws HibernateException {
        return this.deepCopy(o);
    }

}
