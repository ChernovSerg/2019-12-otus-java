package ru.otus.chernovsa.atm;

public class Account {
    private Integer balance = 0;

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
}
