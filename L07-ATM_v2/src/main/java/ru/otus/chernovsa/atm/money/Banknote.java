package ru.otus.chernovsa.atm.money;

public class Banknote implements Cloneable {
    private final NominalValue nominal;
    public final CurrencyCode currency;

    public Banknote(NominalValue nominal, CurrencyCode currency) {
        this.nominal = nominal;
        this.currency = currency;
    }

    public Banknote clone() {
        return new Banknote(this.nominal, this.currency);
    }

    public Integer getCost() {
        return nominal.getValue();
    }

    public NominalValue getNominal() {
        return this.nominal;
    }

    public CurrencyCode getCurrency() {
        return currency;
    }
}
