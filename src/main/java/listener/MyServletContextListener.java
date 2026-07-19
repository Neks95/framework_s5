package main.java.listener;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import main.java.core.MethodeControllerMapping;
import main.java.core.UrlMethodeHttpMapping;
import main.java.exception.ViewParametersNotGivenException;
import main.java.itu.annotation.UrlMapping;
import main.java.utils.Loader;
import main.java.view.GlobalViewParameter;
import main.java.itu.annotation.Controller;

@WebListener
public class MyServletContextListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        HashMap<UrlMethodeHttpMapping, MethodeControllerMapping> mappingUrl = new HashMap<>();
        String packageName = servletContext.getInitParameter("controllerPackage");
        if (packageName == null) {
            packageName = "controller";
        }
        Loader.getUrlMappingByAnnotation(UrlMapping.class, mappingUrl, packageName, Controller.class);

        if (!mappingUrl.isEmpty()) {
            servletContext.setAttribute("mappingUrl", mappingUrl);
            System.out.println(LocalDateTime.now() + " : SCAN DES CONTROLLERS OK");
        } else {
            System.out.println(LocalDateTime.now() + " : ERREUR LORS DU SCAN");
        }
        try {
            String prefix = servletContext.getInitParameter("prefix");
            String suffix = servletContext.getInitParameter("suffix");
            if (prefix == null && suffix == null) {
                throw new ViewParametersNotGivenException();
            } else {
                GlobalViewParameter.setPrefix(prefix);
                GlobalViewParameter.setSuffixe(suffix);
            }
        } catch (ViewParametersNotGivenException v) {
            System.out.println(v.getMessage());
            GlobalViewParameter.setPrefix("/WEB-INF/views/");
            GlobalViewParameter.setSuffixe(".jsp");
        }
        String springPackagesParam = servletContext.getInitParameter("springPackages");
        if (springPackagesParam != null && !springPackagesParam.isBlank()) {
            String[] basePackages = springPackagesParam.split(",");
            for (int i = 0; i < basePackages.length; i++) {
                basePackages[i] = basePackages[i].trim();
            }

            AnnotationConfigApplicationContext springContext = new AnnotationConfigApplicationContext();
            springContext.scan(basePackages);
            springContext.refresh();

            servletContext.setAttribute("springContext", springContext);
            System.out.println(LocalDateTime.now() + " : SPRING CONTEXT DEMARRE avec packages "
                    + String.join(", ", basePackages));
        } else {
            System.out.println(LocalDateTime.now() + " : Aucun package Spring fourni, contexte non démarré");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        Object ctx = servletContext.getAttribute("springContext");
        if (ctx instanceof AnnotationConfigApplicationContext springContext) {
            springContext.close(); 
        }
    }
}