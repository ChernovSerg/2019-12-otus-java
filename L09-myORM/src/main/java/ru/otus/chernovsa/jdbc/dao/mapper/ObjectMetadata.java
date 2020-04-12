package ru.otus.chernovsa.jdbc.dao.mapper;

import ru.otus.chernovsa.core.dao.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ObjectMetadata {
    private String objName;
    private Field fieldWithIdAnnotation;
    private List<Field> fieldWithoutIdAnnotation;

    public ObjectMetadata(Object object) throws Exception {
        objName = object.getClass().getName().substring(object.getClass().getName().lastIndexOf('.') + 1);
        initFields(object);
    }

    public List<Field> getFields() {
        List<Field> result = new ArrayList<>();
        result.add(fieldWithIdAnnotation);
        result.addAll(fieldWithoutIdAnnotation);
        return result;
    }

    public Field getFieldWithIdAnnotation() {
        return fieldWithIdAnnotation;
    }

    public List<Field> getFieldsForInsert() {
        return fieldWithoutIdAnnotation;
    }

    public String getObjName() {
        return objName;
    }

    private void initFields(Object object) throws Exception {
        fieldWithoutIdAnnotation = new ArrayList<>();
        int numIdAnnotation = 0;
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object val = field.get(object);
            if (!isPrimitiveField(val.getClass())) {
                reset();
                throw new ObjectMetadataException("Объект состоит не только из примитивных типов.");
            }
            if (field.isAnnotationPresent(Id.class)) {
                numIdAnnotation++;
                if (numIdAnnotation > 1) {
                    reset();
                    throw new ObjectMetadataException("Объект содержит больше 1 поля с аннотацие Id.");
                }
                fieldWithIdAnnotation = field;
                continue;
            }
            fieldWithoutIdAnnotation.add(field);
        }
        if (numIdAnnotation < 1) {
            reset();
            throw new ObjectMetadataException("Объект НЕ содержит поля с аннотацие Id.");
        }
    }

    private boolean isPrimitiveField(Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz) || String.class.isAssignableFrom(clazz)
                || Character.class.isAssignableFrom(clazz) || char.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz) || boolean.class.isAssignableFrom(clazz);
    }

    private void reset() {
        objName = null;
        fieldWithIdAnnotation = null;
        fieldWithoutIdAnnotation = null;
    }
}
