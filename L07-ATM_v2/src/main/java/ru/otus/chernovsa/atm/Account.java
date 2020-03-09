package ru.otus.chernovsa.atm;

import ru.otus.chernovsa.atm.money.CurrencyCode;

public class Account {
    private final CurrencyCode currency;
    private Integer balance = 0;

    public Account(CurrencyCode currency, Integer balance) {
        this.currency = currency;
        this.balance = balance;
    }

    public void addMoney(Integer amount) {
        balance += amount;
    }

    public boolean takeMoney(Integer amount) {
        if (this.balance < amount) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public Integer getBalance() {
        return balance;
    }

    public CurrencyCode getCurrency() {
        return currency;
    }
}
