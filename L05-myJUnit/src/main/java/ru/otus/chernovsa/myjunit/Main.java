package ru.otus.chernovsa.myjunit;

import ru.otus.chernovsa.myjunit.mytest.MyTest;

@SuppressWarnings("unchecked")
public class Main {
    public static void main(String[] args) {
        TestRunner testRunner = null;
        try {
            testRunner = new TestRunner(MyTest.class);
            testRunner.execute();
            TestStatistics testStatistic = testRunner.getTestStatistic();
            System.out.println(testStatistic.getStatisticsToString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
