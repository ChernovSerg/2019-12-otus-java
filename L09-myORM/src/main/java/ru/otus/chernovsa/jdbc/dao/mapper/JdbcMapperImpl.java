package ru.otus.chernovsa.jdbc.dao.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.chernovsa.core.dao.JdbcMapper;
import ru.otus.chernovsa.core.dao.JdbcMapperException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.otus.chernovsa.jdbc.dao.mapper.SqlRequestType.INSERT_ALL_FIELDS;
import static ru.otus.chernovsa.jdbc.dao.mapper.SqlRequestType.SELECT_ALL_FIELDS_BY_ID;

public class JdbcMapperImpl implements JdbcMapper {
    private static Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);
    private ObjectMetadata metadata;
    private SqlRequests sqlRequests = new SqlRequests();

    @Override
    public String getSqlInsert(Object object) throws JdbcMapperException {
        if (sqlRequests.hasRequest(INSERT_ALL_FIELDS)) {
            return sqlRequests.getRequest(INSERT_ALL_FIELDS);
        }

        if (object == null) {
            throw new JdbcMapperException("Given object is null");
        }

        initMetadata(object);

        String fieldsForInsert = metadata.getFieldsForInsert().stream().map(Field::getName).collect(Collectors.joining(", "));
        String valuesPlaceHolders = String.join(", ", Collections.nCopies(metadata.getFieldsForInsert().size(), "?"));
        String query = String.format("insert into %s (%s) values(%s)", metadata.getObjName(), fieldsForInsert, valuesPlaceHolders);
        sqlRequests.put(INSERT_ALL_FIELDS, query);
        return query;
    }

    @Override
    public List<Object> getParamsForInsert(Object object) throws JdbcMapperException {
        List<Object> result = new ArrayList<>();
        if (object == null) {
            return result;
        }
        initMetadata(object);
        for (Field field : metadata.getFieldsForInsert()) {
            try {
                field.setAccessible(true);
                result.add(field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        logger.info("list params for insert: {}", result);
        return result;
    }

    @Override
    public String getSqlSelect(Class<?> clazz) throws JdbcMapperException {
        if (sqlRequests.hasRequest(SELECT_ALL_FIELDS_BY_ID)) {
            return sqlRequests.getRequest(SELECT_ALL_FIELDS_BY_ID);
        }

        try {
            initMetadata(clazz.getConstructor().newInstance());
        } catch (Exception e) {
            throw new JdbcMapperException(e.getMessage(), e);
        }
        String fieldsForSelect = metadata.getFields().stream().map(Field::getName).collect(Collectors.joining(", "));
        String result = String.format("select %s from %s where %s = ?",
                fieldsForSelect,
                metadata.getObjName(),
                metadata.getFieldWithIdAnnotation().getName());
        sqlRequests.put(SELECT_ALL_FIELDS_BY_ID, result);
        return result;
    }

    @Override
    public <T> Optional<T> getObject(ResultSet rs, Class<T> clazz) throws JdbcMapperException {
        T returnObj;
        try {
            returnObj = clazz.getConstructor().newInstance();
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
        return Optional.of(returnObj);
    }

    private void initMetadata(Object object) throws JdbcMapperException {
        if (metadata == null) {
            try {
                metadata = new ObjectMetadata(object);
            } catch (Exception e) {
                throw new JdbcMapperException(e.getMessage());
            }
        }
    }

}
