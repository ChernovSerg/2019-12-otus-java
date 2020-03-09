package ru.otus.chernovsa.atm;

import org.junit.Before;
import org.junit.Test;
import ru.otus.chernovsa.atm.money.Banknote;
import ru.otus.chernovsa.atm.money.CurrencyCode;
import ru.otus.chernovsa.atm.money.NominalValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class AtmTest {
    Atm atm;
    List<Banknote> banknotes = new ArrayList<>();

    @Before
    public void init() {
        atm = new Atm();
//        banknotes.add(new Banknote(NominalValue.ONE, CurrencyCode.RUB));
        banknotes.add(new Banknote(NominalValue.FIVE, CurrencyCode.RUB));
        banknotes.add(new Banknote(NominalValue.FIVE, CurrencyCode.RUB));
        banknotes.add(new Banknote(NominalValue.FIVE, CurrencyCode.RUB));
    }

    @Test
    public void addBanknote() {
        Banknote banknote = new Banknote(NominalValue.ONE, CurrencyCode.USD);
        assertThat(atm.addBanknote(banknote)).isTrue();
    }

    @Test
    public void removeBanknote() {
        atm.putMoney(banknotes);
        assertThat(atm.removeBanknote(NominalValue.ONE)).isFalse();
        assertThat(atm.removeBanknote(NominalValue.FIVE)).isTrue();
    }

    @Test
    public void putMoneyToAtm() {
        Banknote banknote = new Banknote(NominalValue.ONE_HUNDRED, CurrencyCode.USD);
        banknotes.add(banknote);
        assertThat(atm.putMoney(banknotes)).isFalse();
        banknotes.remove(banknote);
        assertThat(atm.putMoney(banknotes)).isTrue();
    }

    @Test
    public void checkFreeSpaceInCells() {
        assertThat(atm.checkFreeSpaceInCells()).isFalse();

        Banknote banknote = new Banknote(NominalValue.ONE_THOUSAND, CurrencyCode.RUB);
        banknotes.add(banknote);
        atm.putMoney(banknotes);
        assertThat(atm.checkFreeSpaceInCells()).isFalse();

        atm.exit();
        banknotes.remove(banknote);
        Banknote banknote2 = new Banknote(NominalValue.FIVE, CurrencyCode.RUB);
        List<Banknote> banknoteList = Arrays.asList(banknote2, banknote2, banknote2, banknote2, banknote2,
                banknote2, banknote2, banknote2, banknote2, banknote2, banknote2, banknote2);
        banknotes.addAll(banknoteList);
        atm.putMoney(banknotes);
        assertThat(atm.checkFreeSpaceInCells()).isFalse();

        atm.exit();
        banknotes.removeAll(banknoteList);
        atm.putMoney(banknotes);
        assertThat(atm.checkFreeSpaceInCells()).isTrue();
    }

    //test for private method
//    @Test
//    public void checkFreeSpaceInOneCell() {
//        Map<Boolean, Integer> result = new HashMap<>();
//        result = atm.checkFreeSpaceInOneCell(0, 2);
//        assertThat(result.containsKey(true)).isTrue();
//        result = atm.checkFreeSpaceInOneCell(0, 3);
//        assertThat(result.containsKey(true)).isTrue();
//        result = atm.checkFreeSpaceInOneCell(0, 4);
//        assertThat(result.containsKey(true)).isFalse();
//    }

    @Test
    public void transferMoneyToAccount() {
        Account acc = new Account();
        assertThat(atm.transferMoneyToAccount()).isFalse();
        atm.putMoney(banknotes);
        assertThat(atm.transferMoneyToAccount()).isFalse();
        atm.enterAccountNumber(acc);
        assertThat(atm.transferMoneyToAccount()).isTrue();
        assertThat(acc.getBalance()).isEqualTo(15);

    }

    @Test
    public void takeMoney() throws Exception {
        //доводим содержимое банкомата до состояния:
        // 2 купюры по 2 руб
        // 3 купюры по 5 руб
        banknotes.add(new Banknote(NominalValue.TWO, CurrencyCode.RUB));
        banknotes.add(new Banknote(NominalValue.TWO, CurrencyCode.RUB));

        //пробуем снять деньги без указания счета
        List<Banknote> takeMoney = atm.takeMoney(1);
        assertThat(takeMoney.isEmpty()).isTrue();

        //пробуем снять сумму денег бОльшую, чем есть на счете
        Account acc = new Account();
        acc.addMoney(2);
        atm.enterAccountNumber(acc);
        takeMoney = atm.takeMoney(5);
        assertThat(takeMoney.isEmpty()).isTrue();

        atm.putMoney(banknotes);
        assertThat(atm.transferMoneyToAccount()).isTrue();

        //пробуем снять денег больше, чем есть их в банкомате
        acc.addMoney(50);
        takeMoney = atm.takeMoney(40);
        assertThat(takeMoney.isEmpty()).isTrue();

        //пробуем снять дозволенную сумму, но в банкомате не хватает купюр
        Throwable thrown = catchThrowable(() -> {
                    atm.takeMoney(16);
                }
        );
        assertThat(thrown.getMessage()).isNotBlank();

        //снимаем дозволенную сумму
        Integer balance = acc.getBalance();
        takeMoney = atm.takeMoney(9);
        assertThat(takeMoney.stream().map(Banknote::getCost).reduce(Integer::sum).get()).isEqualTo(9);
        assertThat(atm.getAccountBalance()).isEqualTo(balance - 9);
    }

    @Test
    public void getAccountBalance() {
        Integer balance = banknotes.stream().map(Banknote::getCost).reduce(Integer::sum).get();
        assertThat(atm.getAccountBalance()).isEqualTo(0);
        Account a = new Account();
        a.addMoney(100);
        atm.enterAccountNumber(a);
        assertThat(atm.getAccountBalance()).isEqualTo(100);
    }

    //    test for private method
//    @Test
//    public void getAtmBalance() {
//        Account acc = new Account();
//        atm.putMoneyToAtm(banknotes);
//        atm.enterAccountNumber(acc);
//        atm.transferMoneyToAccount();
//        assertThat(atm.getAtmBalance()).isEqualTo(16);
//    }

//    test for private method
//    @Test
//    public void findCell() {
//        assertThat(atm.findCellIndexByNominalAndCurrency(NominalValue.FIVE, CurrencyCode.RUB)).isGreaterThan(0);
//        assertThat(atm.findCellIndexByNominalAndCurrency(NominalValue.FIVE, CurrencyCode.USD)).isEqualTo(-1);
//        assertThat(atm.findCellIndexByNominalAndCurrency(NominalValue.ONE_HUNDRED, CurrencyCode.RUB)).isEqualTo(-1);
//    }
}
