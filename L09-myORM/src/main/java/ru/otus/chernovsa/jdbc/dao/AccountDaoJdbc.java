package ru.otus.chernovsa.jdbc.dao;

import ru.otus.chernovsa.core.dao.AccountDao;
import ru.otus.chernovsa.core.dao.ObjectDao;
import ru.otus.chernovsa.core.model.Account;
import ru.otus.chernovsa.core.sessionmanager.SessionManager;

import java.util.Optional;

public class AccountDaoJdbc implements AccountDao {
    private ObjectDao objectDao;

    public AccountDaoJdbc(ObjectDao objectDao) {
        this.objectDao = objectDao;
    }

    @Override
    public long saveAccount(Account account) {
        return objectDao.saveObject(account);
    }

    @Override
    public Optional<Account> findById(long id) {
        return objectDao.findById(id);
    }

    @Override
    public SessionManager getSessionManager() {
        return objectDao.getSessionManager();
    }
}
