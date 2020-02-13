package ru.otus.chernovsa.classloader;

public class TestLoggingWithoutAnnotation implements MyClassInterface {
    @Override
    public void calculation(int param) {
        System.out.println("calculation, param: " + param);
    }
}
