package main.java.core;

import java.lang.reflect.Method;

public class MethodeControllerMapping {
    private Method methode;
    private Class<?> classe;

    public MethodeControllerMapping(Method m, Class<?> classe) {
        this.methode  = m;
        this.classe = classe;
    }
    public Method getMethod() {
        return methode;
    }
    public void setMethod(Method m) {
        this.methode = m;
    }
    public Class<?> getClasse() {
        return classe;
    }
    public void setClasse(Class<?> classe) {
        this.classe = classe;
    }

    

    
    
}
