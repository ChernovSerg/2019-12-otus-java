package ru.otus.chernovsa.classloader;

public class TestLoggingWithAnnotation implements MyClassInterface {
    @Override
    @Log
    public void calculation(int param) {
        System.out.println("calculation, param: " + param);
    }
}
