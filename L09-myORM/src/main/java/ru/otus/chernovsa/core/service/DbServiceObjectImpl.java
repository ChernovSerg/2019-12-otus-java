package ru.otus.chernovsa.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.chernovsa.core.dao.ObjectDao;
import ru.otus.chernovsa.core.sessionmanager.SessionManager;

import java.util.Optional;

public class DbServiceObjectImpl implements DBServiceObject {
    private static Logger logger = LoggerFactory.getLogger(DbServiceObjectImpl.class);

    private final ObjectDao objectDao;

    public DbServiceObjectImpl(ObjectDao objectDao) {
        this.objectDao = objectDao;
    }

    @Override
    public long saveObject(Object object) {
        try (SessionManager sessionManager = objectDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                long userId = objectDao.saveObject(object);
                sessionManager.commitSession();

                logger.info("created object: {}", userId);
                return userId;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }


    @Override
    public <T> Optional<T> getObject(long id, Class<T> clazz) {
        try (SessionManager sessionManager = objectDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<T> userOptional = objectDao.findById(id);
                logger.info("object: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

}
