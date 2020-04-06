package ru.otus.chernovsa.core.model;

import ru.otus.chernovsa.core.dao.Id;

public class Account {

    @Id
    private final long no;
    private final String type;
    private final double rest;

    public Account() {
        this.no = 0;
        this.type = "";
        this.rest = 0.00;
    }

    public Account(long no, String type, double rest) {
        this.no = no;
        this.type = type;
        this.rest = rest;
    }

    public long getNo() {
        return no;
    }

    public String getType() {
        return type;
    }

    public double getRest() {
        return rest;
    }

    @Override
    public String toString() {
        return "Account{"
                + " no=" + no
                + ", type='" + type + '\''
                + ", rest=" + rest
                + '}';
    }
}
