package ru.otus.chernovsa.jdbc.dao;

import ru.otus.chernovsa.core.dao.ObjectDao;
import ru.otus.chernovsa.core.dao.UserDao;
import ru.otus.chernovsa.core.model.User;
import ru.otus.chernovsa.core.sessionmanager.SessionManager;

import java.util.Optional;

public class UserDaoJdbc implements UserDao {
    private ObjectDao objectDao;

    public UserDaoJdbc(ObjectDao objectDao) {
        this.objectDao = objectDao;
    }

    @Override
    public long saveUser(User user) {
        return objectDao.saveObject(user);
    }

    @Override
    public Optional<User> findById(long id) {
        return objectDao.findById(id);
    }

    @Override
    public SessionManager getSessionManager() {
        return objectDao.getSessionManager();
    }

}
