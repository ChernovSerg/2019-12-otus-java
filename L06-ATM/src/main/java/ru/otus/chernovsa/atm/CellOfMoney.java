package ru.otus.chernovsa.atm;

import ru.otus.chernovsa.atm.money.Banknote;
import ru.otus.chernovsa.atm.money.CurrencyCode;
import ru.otus.chernovsa.atm.money.NominalValue;
import ru.otus.chernovsa.atm.money.PutTakeMoney;

import java.util.ArrayList;
import java.util.List;

public class CellOfMoney implements PutTakeMoney {
    private static final int DEFAULT_SIZE_OF_CELL = 10;

    private final NominalValue nominalValue;
    private final CurrencyCode currencyCode;
    private final int size;
    private List<Boolean> banknotes;

    public CellOfMoney(NominalValue nominalValue) {
        this.nominalValue = nominalValue;
        this.currencyCode = CurrencyCode.RUB;
        this.size = DEFAULT_SIZE_OF_CELL;
        banknotes = new ArrayList<>(size);
    }

    public CellOfMoney(NominalValue nominalValue, CurrencyCode currencyCode, int size) {
        this.nominalValue = nominalValue;
        this.currencyCode = currencyCode;
        this.size = size;
        banknotes = new ArrayList<>(size);
    }

    @Override
    public boolean addBanknote(Banknote banknote) {
        if (banknotes.size() == size || banknote.getNominal() != nominalValue
                || banknote.getCurrency() != currencyCode) {
            return false;
        }
        banknotes.add(true);
        return true;
    }

    @Override
    public boolean removeBanknote(NominalValue nominal) {
        if (banknotes.isEmpty() || nominal != nominalValue) {
            return false;
        }
        banknotes.remove(banknotes.size() - 1);
        return true;
    }

    @Override
    public boolean putMoney(List<Banknote> banknotes) {
        if (banknotes.isEmpty() || banknotes.size() > getFreeCapacity() || !isSameCurrency(banknotes)) {
            return false;
        }
        if (banknotes.get(0).getNominal() == nominalValue) {
            banknotes.forEach(this::addBanknote);
        }
        return true;
    }

    @Override
    public List<Banknote> takeMoney(int amount) {
        throw new UnsupportedOperationException();
    }

    public boolean removeBanknotes(int cntBanknotes) {
        boolean result = false;
        for (int i = 0; i < cntBanknotes; i++) {
            result = this.removeBanknote(nominalValue);
        }
        return result;
    }

    public int getFreeCapacity() {
        return size - banknotes.size();
    }

    public int getSize() {
        return size;
    }

    public int getBookedElements() {
        return banknotes.size();
    }

    public CurrencyCode getCurrencyCode() {
        return currencyCode;
    }

    public NominalValue getNominalValue() {
        return nominalValue;
    }
}
