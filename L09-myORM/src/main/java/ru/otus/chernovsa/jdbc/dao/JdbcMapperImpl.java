package ru.otus.chernovsa.jdbc.dao;

import ru.otus.chernovsa.core.dao.Id;
import ru.otus.chernovsa.core.dao.JdbcMapper;
import ru.otus.chernovsa.core.dao.JdbcMapperException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcMapperImpl implements JdbcMapper {

    @Override
    public String getSqlInsert(Object object) throws JdbcMapperException {
        StringBuilder result = new StringBuilder();
        String objName;
        List<Map<Field, Object>> fields = new ArrayList<>();

        if (object == null) {
            return result.toString();
        }
        objName = object.getClass().getName().substring(object.getClass().getName().lastIndexOf('.') + 1);
        fields = parseObject(object);

        result.append("insert into ").append(objName).append("(");
        for (Map<Field, Object> field : fields) {
            Field fld = (Field) field.keySet().toArray()[0];
            result.append(fld.getName()).append(",");
        }
        result.deleteCharAt(result.length() - 1).append(") values (");
        for (int i = 0; i < fields.size(); i++) {
            result.append("?,");
        }
        result.deleteCharAt(result.length() - 1).append(")");
        return result.toString();
    }

    @Override
    public List<String> getParamsForInsert(Object object) throws JdbcMapperException {
        List<String> result = new ArrayList<>();
        List<Map<Field, Object>> fields = new ArrayList<>();

        if (object == null) {
            return result;
        }
        fields = parseObject(object);
        for (Map<Field, Object> field : fields) {
            Field fld = (Field) field.keySet().toArray()[0];
            Class<?> clazz = null;
            try {
                clazz = fld.get(object).getClass();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            assert clazz != null;
            if (Byte.class.isAssignableFrom(clazz) || Short.class.isAssignableFrom(clazz)
                    || Integer.class.isAssignableFrom(clazz) || Long.class.isAssignableFrom(clazz)
                    || Float.class.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz)
                    || Boolean.class.isAssignableFrom(clazz)
            ) {
                result.add(field.get(fld).toString());
            } else {
                result.add("\"" + field.get(fld).toString() + "\"");
            }
        }
        return result;
    }

    @Override
    public String getSqlSelect(Class<?> clazz) throws JdbcMapperException {
        StringBuilder result = new StringBuilder();
        String objName;
        List<Map<Field, Object>> fields = new ArrayList<>();

        Object object = null;
        try {
            object = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new JdbcMapperException(e.getMessage(), e);
        }
        objName = object.getClass().getName().substring(object.getClass().getName().lastIndexOf('.') + 1);
        fields = parseObject(object);

        String fieldWithIdAnnotation = getFieldNameWithIdAnnotation(object);
        result.append("select ").append(fieldWithIdAnnotation).append(",");
        for (Map<Field, Object> field : fields) {
            Field fld = (Field) field.keySet().toArray()[0];
            result.append(fld.getName()).append(",");
        }
        result.deleteCharAt(result.length() - 1).append(" from ").append(objName)
                .append(" where ").append(fieldWithIdAnnotation).append(" = ?");
        return result.toString();
    }

    @Override
    public <T> Optional<T> getObject(ResultSet rs, Class<T> clazz) throws JdbcMapperException {
        T returnObj;
        try {
            returnObj = (T) clazz.getConstructor().newInstance();
            Field[] fieldsObject = getObjectFields(returnObj);
            Class<?> aClass = returnObj.getClass();
            for (int i = 0; i < fieldsObject.length; i++) {
                String fldName = fieldsObject[i].getName();
                Field field = aClass.getDeclaredField(fldName);
                field.setAccessible(true);
                Object val = rs.getObject(fldName);
                if (BigDecimal.class.isAssignableFrom(val.getClass())) {
                    val = ((BigDecimal) val).doubleValue();
                }
                field.set(returnObj, val);
            }
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException
                | IllegalAccessException | NoSuchFieldException | SQLException e) {
            throw new JdbcMapperException(e.getMessage(), e);
        }
        return Optional.ofNullable(returnObj);
    }

    private Field[] getObjectFields(Object object) throws JdbcMapperException {
        Field[] fields = new Field[object.getClass().getDeclaredFields().length];
        List<Map<Field, Object>> flds = parseObject(object);
        for (int i = 0; i < flds.size(); i++) {
            fields[i + 1] = (Field) flds.get(i).keySet().toArray()[0];
        }
        Field annot = getFieldWithIdAnnotation(object);
        fields[0] = annot;
        return fields;
    }

    private String getFieldNameWithIdAnnotation(Object object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                return field.getName();
            }
        }
        return null;
    }

    private Field getFieldWithIdAnnotation(Object object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        return null;
    }

    /**
     * Парсинг объекта
     *
     * @param object - исходный объект
     * @return List<Map < Field, Object>> - массив полей объекта
     * @throws JdbcMapperException, если объект невалиден
     */
    private List<Map<Field, Object>> parseObject(Object object) throws JdbcMapperException {
        List<Map<Field, Object>> result = new ArrayList<>();
        int numIdAnnotation = 0;
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class) && numIdAnnotation < 1) {
                numIdAnnotation++;
                if (numIdAnnotation > 1) {
                    throw new JdbcMapperException("Объект содержит больше 1 поля с аннотацие Id.");
                }
                continue;
            }
            field.setAccessible(true);
            try {
                Object val = field.get(object);
                if (!isPrimitiveField(val.getClass())) {
                    throw new JdbcMapperException("Объект состоит не только из примитивных типов.");
                } else {
                    result.add(Collections.singletonMap(field, val));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (numIdAnnotation < 1) {
            throw new JdbcMapperException("Объект НЕ содержит поля с аннотацие Id.");
        }
        return result;
    }

    private boolean isPrimitiveField(Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz) || String.class.isAssignableFrom(clazz)
                || Character.class.isAssignableFrom(clazz) || char.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz) || boolean.class.isAssignableFrom(clazz);
    }
}
