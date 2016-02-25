/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.ge.current.innovation.DataPoint;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author lordoftheflies
 */
@Component
public class CsvExternalizationService {

    public <T> List<T> parse(InputStream fis, Class<T> entityType) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(entityType); // schema from 'Pojo' definition
//        String csv = mapper.writer(schema).writeValueAsString(value);

        List<T> result = new ArrayList<T>();

        for (MappingIterator<T> iterator = mapper.readerFor(DataPoint.class).with(schema).readValues(fis); iterator.hasNext();) {
            result.add(iterator.next());
        }

        return result;
    }

    public <T> void serialize(List<T> items, OutputStream fos, Class<T> entityType) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(entityType); // schema from 'Pojo' definition

        mapper.writerFor(entityType).with(schema).writeValues(fos).writeAll(items);
    }
}
