package ru.otus.chernovsa.jdbc.dao.mapper;

import ru.otus.chernovsa.core.dao.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ObjectMetadata<T> {
    private String className;
    private Constructor<T> constructor;
    private Field fieldWithIdAnnotation;
    private List<Field> fieldWithoutIdAnnotation;

    public ObjectMetadata(Class<T> clazz) throws ObjectMetadataException {
        this.className = clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1);
        try {
            this.constructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new ObjectMetadataException(e.getMessage());
        }
        initFields(clazz);
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

    public String getClassName() {
        return className;
    }

    public void initFields(Class<?> clazz) throws ObjectMetadataException {
        fieldWithoutIdAnnotation = new ArrayList<>();
        int numIdAnnotation = 0;
        for (Field field : clazz.getDeclaredFields()) {
            if (!isPrimitiveField(field.getType())) {
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

    public Constructor<T> getConstructor() {
        return constructor;
    }

    private boolean isPrimitiveField(Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz)
                || Character.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz)
                || String.class.isAssignableFrom(clazz)
                || clazz.isPrimitive();
    }

    private void reset() {
        className = null;
        constructor = null;
        fieldWithIdAnnotation = null;
        fieldWithoutIdAnnotation = null;
    }
}
