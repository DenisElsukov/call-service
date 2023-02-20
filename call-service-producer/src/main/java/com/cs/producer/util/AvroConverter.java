package com.cs.producer.util;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.reflect.ReflectData;
import org.springframework.beans.PropertyAccessorFactory;

public class AvroConverter {

    public static GenericData.Record mapObjectToRecord(Object object) {
        final Schema schema = ReflectData.get().getSchema(object.getClass());
        final GenericData.Record record = new GenericData.Record(schema);
        schema.getFields().forEach(r -> record.put(r.name(), PropertyAccessorFactory.forDirectFieldAccess(object).getPropertyValue(r.name())));
        return record;
    }

    public static <T> T mapRecordToObject(GenericData.Record record, T object) {
        final Schema schema = ReflectData.get().getSchema(object.getClass());
        record.getSchema().getFields().forEach(d -> PropertyAccessorFactory.forDirectFieldAccess(object).setPropertyValue(d.name(), record.get(d.name()) == null ? record.get(d.name()) : record.get(d.name()).toString()));
        return object;
    }
}
