package ru.otus.chernovsa.diyarraylist;

import org.junit.Test;

public class DiyArrayListTest {
    @Test
    public void Test() {
        DIYArrayList<Integer> arr = new DIYArrayList<>(5);
        arr.add(5);
        System.out.println("end");
    }
}