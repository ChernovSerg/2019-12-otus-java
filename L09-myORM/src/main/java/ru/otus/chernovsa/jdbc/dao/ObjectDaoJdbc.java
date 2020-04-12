package ru.otus.chernovsa.jdbc.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.chernovsa.core.dao.JdbcMapper;
import ru.otus.chernovsa.core.dao.JdbcMapperException;
import ru.otus.chernovsa.core.dao.ObjectDao;
import ru.otus.chernovsa.core.dao.ObjectDaoException;
import ru.otus.chernovsa.core.sessionmanager.SessionManager;
import ru.otus.chernovsa.jdbc.DbExecutor;
import ru.otus.chernovsa.jdbc.dao.mapper.JdbcMapperImpl;
import ru.otus.chernovsa.jdbc.dao.mapper.ObjectMetadataException;
import ru.otus.chernovsa.jdbc.sessionmanager.SessionManagerJdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class ObjectDaoJdbc<T> implements ObjectDao<T> {
    private static Logger logger = LoggerFactory.getLogger(ObjectDaoJdbc.class);

    private final SessionManagerJdbc sessionManager;
    private final DbExecutor<T> dbExecutor;
    private final JdbcMapper<T> jdbcMapper;

    public ObjectDaoJdbc(SessionManagerJdbc sessionManager, DbExecutor<T> dbExecutor, Class<T> clazz) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
        try {
            this.jdbcMapper = new JdbcMapperImpl<>(clazz);
        } catch (ObjectMetadataException e) {
            throw new ObjectDaoException(e);
        }
    }

    @Override
    public long saveObject(T object) {
        try {
            return dbExecutor.insertRecord(
                    getConnection(),
                    jdbcMapper.getSqlInsert(),
                    jdbcMapper.getParamsForInsert(object)
            );
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ObjectDaoException(e);
        }
    }

    @Override
    public Optional<T> findById(long id) {
        try {
            return dbExecutor.selectRecord(getConnection(),
                    jdbcMapper.getSqlSelect(),
                    id,
                    resultSet -> {
                        try {
                            if (resultSet.next()) {
                                Optional<T> object = jdbcMapper.getObject(resultSet);
                                return object.orElse(null);
                            }
                        } catch (SQLException | JdbcMapperException e) {
                            logger.error(e.getMessage(), e);
                        }
                        return null;
                    });

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}
