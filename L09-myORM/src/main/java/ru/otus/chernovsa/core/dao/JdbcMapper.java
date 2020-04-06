package ru.otus.chernovsa.core.dao;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public interface JdbcMapper {
    String getSqlInsert(Object obj) throws JdbcMapperException;

    List<String> getParamsForInsert(Object obj) throws JdbcMapperException;

    String getSqlSelect(Class<?> clazz) throws JdbcMapperException;

    <T> Optional<T> getObject(ResultSet rs, Class<T> clazz) throws JdbcMapperException;
}
