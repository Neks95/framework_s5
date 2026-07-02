package main.java.utils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.DuplicateFormatFlagsException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.core.MethodeControllerMapping;
import main.java.core.MethodeHttp;
import main.java.core.UrlMethodeHttpMapping;
import main.java.exception.DuplicateUrlAndMethodException;
import main.java.itu.annotation.UrlMapping;

public class Loader {

    public static void getUrlMappingByAnnotation(Class<UrlMapping> annotation,HashMap<UrlMethodeHttpMapping,MethodeControllerMapping> urlMapping , String packageName , Class<? extends Annotation> annotationController) {
        List<Class<?>> listes = new ArrayList<>();
        List<Class<?>> classes = Loader.getClassesInPackage(packageName);
        for (Class<?> classe : classes) {
          if (classe.isAnnotationPresent(annotationController)) {
            listes.add(classe);
            }
        }
        for (Class<?> controller : listes) {
            Method[] methods = controller.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(annotation)) {
                    UrlMapping urlAnnotation = method.getAnnotation(annotation);
                    String url = urlAnnotation.url();
                    MethodeHttp type = MethodeHttp.valueOf(urlAnnotation.methodeHttp().toUpperCase());
                    UrlMethodeHttpMapping currUrlMethodeMapping = new UrlMethodeHttpMapping(url, type);
                    MethodeControllerMapping methodeControllerMapping = new MethodeControllerMapping(method, controller);
                    //miantso an'le equals apres hashage
                    if (urlMapping.containsKey(currUrlMethodeMapping)) {
                        throw new DuplicateUrlAndMethodException(currUrlMethodeMapping);
                    }
                    urlMapping.put(currUrlMethodeMapping, methodeControllerMapping);
                }
            }
        }
    }

    public static List<Class<?>> getClassesInPackage(String packageName) {
        if (packageName == null) {
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