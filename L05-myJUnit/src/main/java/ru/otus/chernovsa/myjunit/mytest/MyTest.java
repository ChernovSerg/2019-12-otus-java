package ru.otus.chernovsa.myjunit.mytest;

import ru.otus.chernovsa.myjunit.annotation.*;

public class MyTest {

    @Before
    public void before() {
        System.out.println("Execute before test.");
    }

    @Test
    public void test1() {
        System.out.println("Execute test1.");
    }

    @Test
    public void test2() throws Exception {
        throw new Exception("Что-то пошло не так!");
    }

    @Test
    public void test3() {
        System.out.println("Execute test3.");
    }

    @Test
    public void test4() throws Exception {
        throw new Exception("Что-то пошло не так!");
    }

    @Test
    public void test5() {
        System.out.println("Execute test5.");
    }

    @After
    public void after() {
        System.out.println("Execute aftre.");
    }


}
