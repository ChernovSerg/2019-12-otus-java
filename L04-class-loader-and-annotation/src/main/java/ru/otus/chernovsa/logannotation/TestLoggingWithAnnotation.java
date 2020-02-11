package ru.otus.chernovsa.logannotation;

public class TestLoggingWithAnnotation implements MyClassInterface {
    @Log
    public void calculation(int param) {
        System.out.println("calculation, param: " + param);
    }
}
