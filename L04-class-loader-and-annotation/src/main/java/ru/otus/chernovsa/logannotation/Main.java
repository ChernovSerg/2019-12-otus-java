package ru.otus.chernovsa.logannotation;

public class Main {
    public static void main(String[] args) {
        TestLoggingWithAnnotation oWithAnnot = new TestLoggingWithAnnotation();
        TestLoggingWithoutAnnotation oWhithoutAnnot = new TestLoggingWithoutAnnotation();
        Test obj = new Test();

        try {
            System.out.println("============================");
            MyClassInterface proxy1 = IoC.createProxy(oWithAnnot, MyClassInterface.class);
            proxy1.calculation(777);
            System.out.println("============================");
            MyClassInterface proxy2 = IoC.createProxy(oWhithoutAnnot, MyClassInterface.class);
            proxy2.calculation(333);
            System.out.println("============================");
            MyClassInterface proxy3 = IoC.createProxy(obj, MyClassInterface.class);
            proxy3.calculation(111);
            System.out.println("============================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
