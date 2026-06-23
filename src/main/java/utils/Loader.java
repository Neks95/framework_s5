package main.java.utils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.java.itu.annotation.UrlMapping; 


public class Loader {

    public static List<Class<?>> getAnnotatedClasses(String packageName,Class<? extends Annotation> annotation){
        List<Class<?>> listes = new ArrayList<>();
        List<Class<?>> classes = Loader.getClassesInPackage(packageName);
        for(Class<?> classe : classes){
            if(classe.isAnnotationPresent(annotation)){
                listes.add(classe);
            }
        }
        return listes;
    }

    public static HashMap<String,MethodeControllerMapping> getMethodByAnnotation(Class<UrlMapping> mapping,List<Class<?>> controllers){
        HashMap<String,MethodeControllerMapping> urlMethod = new HashMap<>();
        for(Class<?> controller : controllers){
            Method[] methods = controller.getDeclaredMethods();
            for(Method m : methods){
                if(m.isAnnotationPresent(mapping)){
                    String url = m.getAnnotation(mapping).url();
                    MethodeControllerMapping methodeControllerMapping = new MethodeControllerMapping(m,controller);
                    urlMethod.put(url,methodeControllerMapping);
                }
            }
        }
        return urlMethod;
        
    }

    public static List<Class<?>> getClassesInPackage (String packageName) {
        if(packageName == null){
            packageName = "org.controller";
        }
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        URL resource = Thread.currentThread()
                             .getContextClassLoader()
                             .getResource(path);
        if (resource == null) {
            throw new RuntimeException("Package introuvable : " + packageName);
        }

        File directory = new File(resource.getFile());

        if (!directory.exists() || !directory.isDirectory()) {
            throw new RuntimeException("Ce n'est pas un package valide : " + packageName);
        }

        File[] files = directory.listFiles();

        if (files == null) {
            return classes;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String className = packageName + "."
                        + file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(
                        "Impossible de charger " + className, e);
                }
            }
        }
        return classes;
    }
}