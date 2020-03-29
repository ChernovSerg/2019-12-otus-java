package ru.otus.chernovsa.myjsonwriter.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Other2 {
    private int n = 5;
    private boolean flag = true;
    List<Integer> list = Arrays.asList(1, 2, 3);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Other2)) return false;
        Other2 other2 = (Other2) o;
        return n == other2.n &&
                flag == other2.flag &&
                list.equals(other2.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(n, flag, list);
    }

    @Override
    public String toString() {
        return "Other2{"
                + " n=" + n
                + ", flag=" + flag
                + ", list=" + list
                + '}';
    }
}

