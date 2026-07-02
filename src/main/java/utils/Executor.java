package main.java.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Executor {
    public static void invokeFunction(Method m ){
        Class<?> proprio = m.getDeclaringClass();
         try  {
            Constructor<?> cons = proprio.getDeclaredConstructor();
            m.invoke(cons.newInstance());
         } catch (Exception e) {
            throw new RuntimeException(e);
         }

    }
    
}
