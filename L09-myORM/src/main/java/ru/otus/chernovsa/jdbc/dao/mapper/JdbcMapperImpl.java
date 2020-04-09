package ru.otus.chernovsa.jdbc.dao.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.chernovsa.core.dao.Id;
import ru.otus.chernovsa.core.dao.JdbcMapper;
import ru.otus.chernovsa.core.dao.JdbcMapperException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static ru.otus.chernovsa.jdbc.dao.mapper.SqlRequestType.INSERT_ALL_FIELDS;
import static ru.otus.chernovsa.jdbc.dao.mapper.SqlRequestType.SELECT_ALL_FIELDS_BY_ID;

public class JdbcMapperImpl implements JdbcMapper {
    private static Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);
    private final ObjectMetadata metadata = new ObjectMetadata();
    private Map<SqlRequestType, String> sqlRequests = new HashMap<>();

    @Override
    public String getSqlInsert(Object object) throws JdbcMapperException {
        if (hasRequest(INSERT_ALL_FIELDS)) {
            return sqlRequests.get(INSERT_ALL_FIELDS);
        }

        StringBuilder result = new StringBuilder();
        String objName;
        if (object == null) {
            return result.toString();
        }
        objName = object.getClass().getName().substring(object.getClass().getName().lastIndexOf('.') + 1);
        initMetadata(object);
        List<String> fieldsForInsert = metadata.getFieldsForInsert().stream().map(Field::getName).collect(Collectors.toList());
        result.append("insert into ").append(objName).append("(");
        for (String filed : fieldsForInsert) {
            result.append(filed).append(",");
        }
        result.deleteCharAt(result.length() - 1).append(") values (");
        result.append("?,".repeat(fieldsForInsert.size()));
        result.deleteCharAt(result.length() - 1).append(")");
        sqlRequests.put(INSERT_ALL_FIELDS, result.toString());
        return result.toString();
    }

    @Override
    public List<String> getParamsForInsert(Object object) throws JdbcMapperException {
        List<String> result = new ArrayList<>();
        if (object == null) {
            return result;
        }
        initMetadata(object);
        List<Field> fields = metadata.getFieldsForInsert();
        for (Field field : fields) {
            try {
                Class<?> clazz = field.get(object).getClass();
                assert clazz != null;
                String val = field.get(object).toString();
                if (Byte.class.isAssignableFrom(clazz) || Short.class.isAssignableFrom(clazz)
                        || Integer.class.isAssignableFrom(clazz) || Long.class.isAssignableFrom(clazz)
                        || Float.class.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz)
                        || Boolean.class.isAssignableFrom(clazz)
                ) {
                    result.add(val);
                } else {
                    result.add("\"" + val + "\"");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        logger.info("list params for insert: {}", result);
        return result;
    }

    @Override
    public String getSqlSelect(Class<?> clazz) throws JdbcMapperException {
        if (hasRequest(SELECT_ALL_FIELDS_BY_ID)) {
            return sqlRequests.get(SELECT_ALL_FIELDS_BY_ID);
        }

        StringBuilder result = new StringBuilder();
        try {
            initMetadata(clazz.getConstructor().newInstance());
        } catch (Exception e) {
            throw new JdbcMapperException(e.getMessage(), e);
        }
        List<Field> fields = metadata.getFields();
        result.append("select ");
        for (Field field : fields) {
            result.append(field.getName()).append(",");
        }
        result.deleteCharAt(result.length() - 1).append(" from ")
                .append(metadata.getObjName())
                .append(" where ")
                .append(metadata.getFieldWithIdAnnotation().getName())
                .append(" = ?");
        sqlRequests.put(SELECT_ALL_FIELDS_BY_ID, result.toString());
        return result.toString();
    }

    @Override
    public <T> Optional<T> getObject(ResultSet rs, Class<T> clazz) throws JdbcMapperException {
        T returnObj;
        try {
            returnObj = (T) clazz.getConstructor().newInstance();
            initMetadata(returnObj);
            List<Field> fields = metadata.getFields();
            for (Field field : fields) {
                String fldName = field.getName();
                Object val = rs.getObject(fldName);
                if (BigDecimal.class.isAssignableFrom(val.getClass())) {
                    val = ((BigDecimal) val).doubleValue();
                }
                field.setAccessible(true);
                field.set(returnObj, val);
            }
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException
                | IllegalAccessException | SQLException e) {
            throw new JdbcMapperException(e.getMessage(), e);
        }
        return Optional.ofNullable(returnObj);
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
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                numIdAnnotation++;
                if (numIdAnnotation > 1) {
                    throw new JdbcMapperException("Объект содержит больше 1 поля с аннотацие Id.");
                }
            }
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

    private void initMetadata(Object object) throws JdbcMapperException {
        if (metadata.getFields() == null) {
            metadata.setObjName(object.getClass().getName().substring(object.getClass().getName().lastIndexOf('.') + 1));
            metadata.setFields(parseObject(object));
        }
    }

    private boolean hasRequest(SqlRequestType requestType) {
        for (SqlRequestType sqlRequestType : sqlRequests.keySet()) {
            if (requestType.equals(sqlRequestType)) {
                return true;
            }
        }
        return false;
    }
}
