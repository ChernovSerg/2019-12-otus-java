package ru.otus.chernovsa.core.dao;

import ru.otus.chernovsa.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface ObjectDao {
    long saveObject(Object object);

    <T> Optional<T> findById(long id);

    SessionManager getSessionManager();
}
