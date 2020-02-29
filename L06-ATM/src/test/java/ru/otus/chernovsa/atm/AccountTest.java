package ru.otus.chernovsa.atm;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountTest {
    private Account acc;

    @Before
    public void init() {
        acc = new Account();
    }

    @Test
    public void addMoney() {
        acc.addMoney(6);
        assertThat(acc.getBalance()).isEqualTo(6);
    }

    @Test
    public void takeMoney() {
        acc.addMoney(6);
        assertThat(acc.takeMoney(2)).isTrue();
        assertThat(acc.getBalance()).isEqualTo(4);
    }

}