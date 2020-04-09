package ru.otus.chernovsa.jdbc.dao.mapper;

import ru.otus.chernovsa.core.dao.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectMetadata {
    private String objName;
    private Field fieldWithIdAnnotation;
    private List<Field> fields;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Map<Field, Object>> objectFieldsWithValues) {
        fields = new ArrayList<>();
        for (Map<Field, Object> fieldObjectMap : objectFieldsWithValues) {
            Field field = (Field) fieldObjectMap.keySet().toArray()[0];
            fields.add(field);
            if (field.isAnnotationPresent(Id.class)) {
                fieldWithIdAnnotation = field;
            }
        }
    }

    public Field getFieldWithIdAnnotation() {
        return fieldWithIdAnnotation;
    }

    public List<Field> getFieldsForInsert() {
        List<Field> result = new ArrayList<>();
        for (Field field : this.fields) {
            if (!field.isAnnotationPresent(Id.class)) {
                result.add(field);
            }
        }
        return result;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }
}
