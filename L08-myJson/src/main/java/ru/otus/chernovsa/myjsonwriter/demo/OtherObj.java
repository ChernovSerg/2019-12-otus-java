package ru.otus.chernovsa.myjsonwriter.demo;

import java.util.Objects;

class OtherObj {
    private String str;
    private Integer k;
    private Other2 other2 = new Other2();

    public OtherObj(String str, Integer k) {
        this.str = str;
        this.k = k;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OtherObj)) return false;
        OtherObj otherObj = (OtherObj) o;
        return Objects.equals(str, otherObj.str) &&
                Objects.equals(k, otherObj.k) &&
                Objects.equals(other2, otherObj.other2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(str, k, other2);
    }

    @Override
    public String toString() {
        return "OtherObj{"
                + " str='" + str + '\''
                + ", k=" + k
                + ", other2=" + other2
                + '}';
    }
}

