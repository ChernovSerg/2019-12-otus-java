package ru.otus.chernovsa.core.service;

import ru.otus.chernovsa.core.model.Account;

import java.util.Optional;

public interface DbServiceAccount {

    long saveAccount(Account object);

    Optional<Account> getAccount(long id);

}
