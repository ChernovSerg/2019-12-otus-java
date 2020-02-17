package ru.otus.chernovsa.myjunit;

import ru.otus.chernovsa.myjunit.annotation.After;
import ru.otus.chernovsa.myjunit.annotation.Before;
import ru.otus.chernovsa.myjunit.annotation.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static ru.otus.chernovsa.myjunit.TestStatus.FAILED;
import static ru.otus.chernovsa.myjunit.TestStatus.SUCCESSFULLY;

public class TestRunner<T> {
    private Class<T> clazz;
    private List<Method> testMethods = new ArrayList<>();
    private Method beforeMethod;
    private Method afterMethod;
    private TestStatistics testStatistic = new TestStatistics();

    public TestRunner(Class<T> clazz) throws TestRunnerException {
        this.clazz = clazz;

        try {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getDeclaredAnnotation(Before.class) != null) {
                    beforeMethod = method;
                    continue;
                }
                if (method.getDeclaredAnnotation(Test.class) != null) {
                    testMethods.add(method);
                    continue;
                }
                if (method.getDeclaredAnnotation(After.class) != null) {
                    afterMethod = method;
                }
            }
            if (testMethods.size() == 0) {
                System.err.println("Class " + clazz.getName() + " does not contain test methods.");
            }
        } catch (Exception e) {
            throw new TestRunnerException("Что-то пошло не так при создании TestRunner", new Throwable(e.getMessage()));
        }
    }

    public void execute() {
        for (Method testMethod : testMethods) {
            T instance = null;
            try {
                instance = clazz.getConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                System.out.println("----------------------------");
                beforeMethod.invoke(instance);
                testMethod.invoke(instance);
                testStatistic.addTestResults(testMethod.getName(), SUCCESSFULLY);
            } catch (Exception e) {
                testStatistic.addTestResults(testMethod.getName(), FAILED);
            } finally {
                try {
                    afterMethod.invoke(instance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public TestStatistics getTestStatistic() {
        return testStatistic;
    }

}
