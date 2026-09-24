package com.onatarslan.springdata.todo;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PriorityConverter implements AttributeConverter<Priority, Short> {

    @Override
    public Short convertToDatabaseColumn(Priority priority) {
        return priority == null ? null : (short) priority.value();
    }

    @Override
    public Priority convertToEntityAttribute(Short value) {
        return value == null ? null : new Priority(value);
    }
}
