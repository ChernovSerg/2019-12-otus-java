package ru.otus.chernovsa.atm.money;

public enum NominalValue {
    ONE(1),
    TWO(2),
    FIVE(5),
    TEN(10),
    TWENTY(20),
    FIFTY(50),
    ONE_HUNDRED(100),
    TWO_HUNDRED(200),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000),
    TWO_THOUSAND(2000);

    private int cost;

    NominalValue(Integer cost) {
        this.cost = cost;
    }

    public Integer getValue() {
        return this.cost;
    }

    @Override
    public String toString() {
        return String.valueOf(cost);
    }
}