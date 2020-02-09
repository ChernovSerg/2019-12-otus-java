package ru.otus.chernovsa.classloader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MyClassLoaderDemo {
    public static void main(String[] args) throws Exception {
        new MyClassLoaderDemo().start();
    }

    @SuppressWarnings("unchecked")
    private void start() throws Exception {
        MyClassLoader loader = new MyClassLoader();
        Class<?> clazzWithAnnot = loader.defineClass("ru.otus.chernovsa.classloader.TestLoggingWithAnnotation");
        Class<?> clazzWithoutAnnot = loader.defineClass("ru.otus.chernovsa.classloader.TestLoggingWithoutAnnotation");

        if (MyClassInterface.class.isAssignableFrom(clazzWithAnnot)) {
            System.out.println("------");
            MyClassInterface proxy1 = IoC.createProxy((Class<? extends MyClassInterface>) clazzWithAnnot);
            proxy1.calculation(111);
            System.out.println("------");
            MyClassInterface proxy2 = IoC.createProxy((Class<? extends MyClassInterface>) clazzWithoutAnnot);
            proxy2.calculation(888);
            System.out.println("------");
        }
    }


    private static class MyClassLoader extends ClassLoader {
        Class<?> defineClass(String className) throws IOException {
            File file = new File(getFile(className));
            byte[] bytecode = Files.readAllBytes(file.toPath());
            return super.defineClass(className, bytecode, 0, bytecode.length);
        }

        String getFile(String className) {
            return "L04-class-loader-and-annotation/myClass/" + className.substring(className.lastIndexOf(".") + 1) + ".class";
        }

    }
}
