package ru.otus.chernovsa.myjunit;

import ru.otus.chernovsa.myjunit.annotation.After;
import ru.otus.chernovsa.myjunit.annotation.Before;
import ru.otus.chernovsa.myjunit.annotation.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TestRunner<T> {
    private Class<T> clazz;
    private List<T> listInstances = new ArrayList<>();
    private List<String> testMethods = new ArrayList<>();
    private String beforeMethod;
    private String afterMethod;
    private Map<String, StatusTest> testStatistic = new TreeMap<>(String::compareTo);

    enum StatusTest {SUCCESSFULLY, FAILED}

    public TestRunner(Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        this.clazz = clazz;
        Constructor<T> constructor = clazz.getConstructor();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (isMethodHasAnnotation(method, Test.class)) {
                testMethods.add(method.getName());
                listInstances.add(constructor.newInstance());
            }
            if (isMethodHasAnnotation(method, Before.class)) {
                beforeMethod = method.getName();
            }
            if (isMethodHasAnnotation(method, After.class)) {
                afterMethod = method.getName();
            }
        }
        if (testMethods.size() == 0) {
            System.err.println("Class " + clazz.getName() + " does not contain test methods.");
        }
    }

    public void execute() {
        for (int i = 0; i < listInstances.size(); i++) {
            try {
                Method method = clazz.getMethod(beforeMethod);
                method.invoke(listInstances.get(i));
                method = clazz.getMethod(testMethods.get(i));
                method.invoke(listInstances.get(i));
                method = clazz.getMethod(afterMethod);
                method.invoke(listInstances.get(i));
                testStatistic.put(testMethods.get(i), StatusTest.SUCCESSFULLY);
            } catch (Exception e) {
                testStatistic.put(testMethods.get(i), StatusTest.FAILED);
            }
        }
    }

    public String getStatisticsToString() {
        StringBuilder result = new StringBuilder();
        result.append("====================\n");
        result.append("Test results:\n");
        result.append("====================\n");
        result.append("All tests ").append(testMethods.size()).append("\n");
        int successfully = Collections.frequency(testStatistic.values(), StatusTest.SUCCESSFULLY);
        result.append("Successful tests - ").append(successfully).append("\n");
        int failed = Collections.frequency(testStatistic.values(), StatusTest.FAILED);
        result.append("Failed tests - ").append(failed).append("\n");
        result.append("--------------------\n");
        result.append("Detailed statistics:\n");
        result.append("--------------------\n");
        for (Map.Entry<String, StatusTest> entry : testStatistic.entrySet()) {
            result.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
        }
        return result.toString();
    }

    private boolean isMethodHasAnnotation(Method method, Class<?> verifiableAnnotation) {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getName().equals(verifiableAnnotation.getName())) {
                return true;
            }
        }
        return false;
    }

}
