/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.innovation.persistence.ext;

import java.util.UUID;
import javax.persistence.AttributeConverter;

/**
 *
 * @author Hegedűs László (212429780)
 */
@javax.persistence.Converter(autoApply = true)
public class UuidConverter implements AttributeConverter<UUID, Object> {

    public Object convertToDatabaseColumn(UUID attribute) {
        return attribute;
    }

    public UUID convertToEntityAttribute(Object dbData) {
        return UUID.randomUUID();
    }
}
