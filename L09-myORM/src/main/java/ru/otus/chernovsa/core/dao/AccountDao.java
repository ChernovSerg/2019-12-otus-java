package ru.otus.chernovsa.core.dao;

import ru.otus.chernovsa.core.model.Account;
import ru.otus.chernovsa.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface AccountDao {
    long saveAccount(Account object);

    Optional<Account> findById(long id);

    SessionManager getSessionManager();
}
