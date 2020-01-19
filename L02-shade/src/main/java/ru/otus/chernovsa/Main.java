package ru.otus.chernovsa;

import java.util.ArrayList;
import java.util.List;

/*
Useful link:
https://maven.apache.org/plugins/maven-shade-plugin/index.html
 */
public class Main {
    public static void main(String[] args) {
        List<Integer> example = new ArrayList<>();
        System.out.println(example);
        System.out.println("Hello, World. Shade test!");
    }
}
