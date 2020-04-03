package ru.otus.chernovsa.logannotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class IoC {
    @SuppressWarnings("unchecked")
    static <T, V> V createProxy(T instance, Class<V> intrf) throws Exception {
        if (!intrf.isAssignableFrom(instance.getClass())) {
            throw new Exception("A proxy class cannot be created because class instance does not implement an interface");
        }
        InvocationHandler handler = new DemoInvocationHandler<>(instance);
        return (V) Proxy.newProxyInstance(IoC.class.getClassLoader(),
                new Class<?>[]{intrf}, handler);
    }

    private static class DemoInvocationHandler<T> implements InvocationHandler {
        private final T myClass;
        //TODO доработать так, чтобы не надо было каждый раз шерстить аннотации метода

        DemoInvocationHandler(T instance) {
            this.myClass = instance;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isNeedLoggingMethod(method)) {
                System.out.println("invoking method: " + method);
            }
            return method.invoke(myClass, args);
        }

        private boolean isNeedLoggingMethod(Method method) throws NoSuchMethodException {
            Annotation[] annotations = this.myClass.getClass().getMethod(method.getName(), method.getParameterTypes()).getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().getName().equals(Log.class.getName())) {
                    return true;
                }
            }
            return false;
        }
    }
}
