package ru.otus.chernovsa.atm;

import ru.otus.chernovsa.atm.money.Banknote;
import ru.otus.chernovsa.atm.money.CurrencyCode;
import ru.otus.chernovsa.atm.money.NominalValue;

public class CellOfMoney {
    private static final int DEFAULT_SIZE_OF_CELL = 10;

    private final NominalValue nominalValue;
    private final CurrencyCode currencyCode;
    private final int size;
    private int bookedElements;
    //решил, что массив типа Banknote хранить ни к чему, т.к. и так понятно, банкноты какого номинала хрянятся в ячейке
    private boolean[] banknotes;

    public CellOfMoney(NominalValue nominalValue) {
        this.nominalValue = nominalValue;
        this.size = DEFAULT_SIZE_OF_CELL;
        banknotes = new boolean[size];
        this.currencyCode = CurrencyCode.RUB;
    }

    public CellOfMoney(NominalValue nominalValue, CurrencyCode currencyCode, int size) {
        this.nominalValue = nominalValue;
        this.currencyCode = currencyCode;
        this.size = size;
        banknotes = new boolean[size];
    }

    public boolean add(Banknote banknote) {
        if (bookedElements == size || !banknote.getCost().equals(nominalValue.getValue())
                || !banknote.getCurrency().equals(this.currencyCode)) {
            return false;
        }
        banknotes[bookedElements++] = true;
        return true;
    }

    public boolean removeBanknote() {
        if (bookedElements == 0) {
            return false;
        }
        banknotes[--bookedElements] = false;
        return true;
    }

    public boolean removeBanknotes(int cntBanknotes) {
        boolean result = false;
        for (int i = 0; i < cntBanknotes; i++) {
            result = this.removeBanknote();
        }
        return result;
    }

    public int getFreeCapacity() {
        return size - bookedElements;
    }

    public int getSize() {
        return banknotes.length;
    }

    public int getBookedElements() {
        return bookedElements;
    }

    public CurrencyCode getCurrencyCode() {
        return currencyCode;
    }

    public NominalValue getNominalValue() {
        return nominalValue;
    }
}
