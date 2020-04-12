package ru.otus.chernovsa.core.dao;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public interface JdbcMapper<T> {
    String getSqlInsert() throws JdbcMapperException;

    String getSqlSelect() throws JdbcMapperException;

    List<Object> getParamsForInsert(T obj) throws JdbcMapperException;

    Optional<T> getObject(ResultSet rs) throws JdbcMapperException;
}
