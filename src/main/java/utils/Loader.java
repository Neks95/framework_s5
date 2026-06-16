package main.java.utils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    public static List<String> getAnnotatedClasses(String packageName,Class<? extends Annotation> annotation){
        List<String> listes = new ArrayList<>();
        List<Class<?>> classes = Loader.getClassesInPackage(packageName);
        for(Class<?> classe : classes){
            if(classe.isAnnotationPresent(annotation)){
                listes.add(classe.getName());
            }
        }
        return listes;
    }

    public static List<Class<?>> getClassesInPackage (String packageName) {
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