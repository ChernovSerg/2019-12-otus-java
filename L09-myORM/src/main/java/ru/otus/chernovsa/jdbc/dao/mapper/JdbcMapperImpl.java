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

public class JdbcMapperImpl<T> implements JdbcMapper<T> {
    private static Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);
    private ObjectMetadata<T> metadata;
    private SqlRequests sqlRequests = new SqlRequests();

    public JdbcMapperImpl(Class<T> clazz) throws ObjectMetadataException {
        metadata = new ObjectMetadata<>(clazz);
    }

    @Override
    public String getSqlInsert() {
        if (sqlRequests.hasRequest(INSERT_ALL_FIELDS)) {
            return sqlRequests.getRequest(INSERT_ALL_FIELDS);
        }
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
            throw  new JdbcMapperException("Given object is null");
        }
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
    public String getSqlSelect() {
        if (sqlRequests.hasRequest(SELECT_ALL_FIELDS_BY_ID)) {
            return sqlRequests.getRequest(SELECT_ALL_FIELDS_BY_ID);
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
    public Optional<T> getObject(ResultSet rs) throws JdbcMapperException {
        T returnObj;
        try {
            returnObj = metadata.getClazz().getConstructor().newInstance();
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
}
