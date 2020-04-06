package ru.otus.chernovsa.core.service;

import java.util.Optional;

public interface DBServiceObject {

    long saveObject(Object object);

    <T> Optional<T> getObject(long id, Class<T> clazz);

}
