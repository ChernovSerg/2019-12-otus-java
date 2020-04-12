package ru.otus.chernovsa.core.dao;

import ru.otus.chernovsa.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface ObjectDao<T> {
    long saveObject(T object);

    Optional<T> findById(long id);

    SessionManager getSessionManager();
}
