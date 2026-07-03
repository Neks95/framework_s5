package main.java.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import main.java.core.MethodeControllerMapping;
import main.java.core.UrlMethodeHttpMapping;

public class Executor {
    public static void invokeFunction(MethodeControllerMapping m ){
         try  {
            Constructor<?> cons = m.getClasse().getDeclaredConstructor();
            m.getMethod().invoke(cons.newInstance());
         } catch (Exception e) {
            throw new RuntimeException(e);
         }

    }
    
}
