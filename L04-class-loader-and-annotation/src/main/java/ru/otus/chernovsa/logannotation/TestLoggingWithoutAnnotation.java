package ru.otus.chernovsa.logannotation;

public class TestLoggingWithoutAnnotation implements MyClassInterface {
    public void calculation(int param) {
        System.out.println("calculation, param: " + param);
    }
}
