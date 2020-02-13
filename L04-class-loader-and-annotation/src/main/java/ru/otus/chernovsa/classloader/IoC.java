package ru.otus.chernovsa.classloader;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class IoC {
    static MyClassInterface createProxy(Class<? extends MyClassInterface> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        InvocationHandler handler = new DemoInvocationHandler(clazz.getConstructor().newInstance());

        return (MyClassInterface) Proxy.newProxyInstance(IoC.class.getClassLoader(),
                new Class<?>[]{MyClassInterface.class}, handler);
    }

    private static class DemoInvocationHandler<T extends MyClassInterface> implements InvocationHandler {
        private final MyClassInterface myClass;

        DemoInvocationHandler(T myClass) {
            this.myClass = myClass;
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
