package ru.otus.chernovsa.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.chernovsa.core.dao.AccountDao;
import ru.otus.chernovsa.core.model.Account;
import ru.otus.chernovsa.core.sessionmanager.SessionManager;

import java.util.Optional;

public class DbServiceAccountImpl implements DbServiceAccount {
    private static Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final AccountDao accountDao;

    public DbServiceAccountImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public long saveAccount(Account object) {
        try (SessionManager sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                long userId = accountDao.saveAccount(object);
                sessionManager.commitSession();

                logger.info("created Account with ID: {}", userId);
                return userId;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public long updateAccount(Account object) {
        try (SessionManager sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                long accId = accountDao.updateAccount(object);
                sessionManager.commitSession();
                logger.info("updated Account with ID: {}", accId);
                return accId;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<Account> getAccount(long id) {
        try (SessionManager sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<Account> userOptional = accountDao.findById(id);
                logger.info("found Account: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}
