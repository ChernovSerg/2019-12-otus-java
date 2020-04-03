package ru.otus.chernovsa.atm.money;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface Money {
    boolean addBanknote(Banknote banknote);

    boolean removeBanknote(NominalValue nominal);

    boolean putMoney(List<Banknote> banknotes);

    List<Banknote> takeMoney(int amount) throws Exception;

    default boolean isSameCurrency(List<Banknote> banknotes) {
        Map<CurrencyCode, List<Banknote>> currencyCodeListMap = banknotes.stream().collect(Collectors.groupingBy(Banknote::getCurrency));
        return currencyCodeListMap.keySet().size() == 1;
    }
}
