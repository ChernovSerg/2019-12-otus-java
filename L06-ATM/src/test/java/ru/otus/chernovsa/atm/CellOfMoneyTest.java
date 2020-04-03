package ru.otus.chernovsa.atm;

import org.junit.Test;
import ru.otus.chernovsa.atm.money.Banknote;
import ru.otus.chernovsa.atm.money.CurrencyCode;
import ru.otus.chernovsa.atm.money.NominalValue;

import static org.assertj.core.api.Assertions.assertThat;

public class CellOfMoneyTest {

    @Test
    public void getNominalValue() {
        CellOfMoney cell = new CellOfMoney(NominalValue.FIVE);
        assertThat(cell.getNominalValue()).isEqualTo(NominalValue.FIVE);
    }

    @Test
    public void addBanknote() {
        CellOfMoney cell = new CellOfMoney(NominalValue.FIVE, CurrencyCode.RUB, 3);
        boolean result;

        Banknote banknoteUSD = new Banknote(NominalValue.FIVE, CurrencyCode.USD);
        result = cell.addBanknote(banknoteUSD);
        assertThat(result).isFalse();

        Banknote banknoteOne = new Banknote(NominalValue.ONE, CurrencyCode.RUB);
        result = cell.addBanknote(banknoteOne);
        assertThat(result).isFalse();
        assertThat(cell.getFreeCapacity()).isEqualTo(cell.getSize());

        Banknote banknoteFive = new Banknote(NominalValue.FIVE, CurrencyCode.RUB);
        result = cell.addBanknote(banknoteFive);
        assertThat(result).isTrue();
        assertThat(cell.getFreeCapacity()).isEqualTo(cell.getSize() - 1);

        assertThat(cell.addBanknote(banknoteFive)).isTrue();
        assertThat(cell.addBanknote(banknoteFive)).isTrue();

        assertThat(cell.addBanknote(banknoteFive)).isFalse();
        assertThat(cell.getFreeCapacity()).isEqualTo(0);
    }

    @Test
    public void removeBanknote() {
        CellOfMoney cell = new CellOfMoney(NominalValue.FIVE, CurrencyCode.RUB, 3);
        Banknote banknoteFive = new Banknote(NominalValue.FIVE, CurrencyCode.RUB);
        cell.addBanknote(banknoteFive);
        cell.addBanknote(banknoteFive);
        cell.addBanknote(banknoteFive);

        assertThat(cell.removeBanknote(NominalValue.FIVE)).isTrue();
        assertThat(cell.getFreeCapacity()).isEqualTo(1);

        assertThat(cell.removeBanknote(NominalValue.FIVE)).isTrue();
        assertThat(cell.getFreeCapacity()).isEqualTo(2);

        assertThat(cell.removeBanknote(NominalValue.FIVE)).isTrue();
        assertThat(cell.getFreeCapacity()).isEqualTo(3);

        assertThat(cell.removeBanknote(NominalValue.FIVE)).isFalse();
    }

    @Test
    public void removeSomeBanknotes() {
        CellOfMoney cell = new CellOfMoney(NominalValue.FIVE, CurrencyCode.RUB, 3);
        Banknote banknoteFive = new Banknote(NominalValue.FIVE, CurrencyCode.RUB);
        cell.addBanknote(banknoteFive);
        cell.addBanknote(banknoteFive);
        cell.addBanknote(banknoteFive);

        assertThat(cell.removeBanknotes(2)).isTrue();
        assertThat(cell.getFreeCapacity()).isEqualTo(2);
        assertThat(cell.getBookedElements()).isEqualTo(1);
    }

    @Test
    public void getCurrencyCode() {
        CellOfMoney cell = new CellOfMoney(NominalValue.FIVE, CurrencyCode.USD, 5);
        assertThat(cell.getCurrencyCode().equals(CurrencyCode.RUB)).isFalse();
        assertThat(cell.getCurrencyCode().equals(CurrencyCode.USD)).isTrue();
    }
}