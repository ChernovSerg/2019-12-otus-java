package ru.otus.chernovsa.core.service;

import ru.otus.chernovsa.core.model.User;

import java.util.Optional;

public interface DbServiceUser {

    long saveUser(User object);

    Optional<User> getUser(long id);

    long updateUser(User object);
}
