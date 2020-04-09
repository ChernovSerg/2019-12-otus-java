package ru.otus.chernovsa.core.dao;

import ru.otus.chernovsa.core.model.User;
import ru.otus.chernovsa.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface UserDao {
    long saveUser(User object);

    Optional<User> findById(long id);

    SessionManager getSessionManager();
}
