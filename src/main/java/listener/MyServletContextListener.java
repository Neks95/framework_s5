package main.java.listener;

import java.time.LocalDateTime;
import java.util.HashMap;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import main.java.core.MethodeControllerMapping;
import main.java.core.UrlMethodeHttpMapping;
import main.java.itu.annotation.UrlMapping;
import main.java.utils.Loader;
import main.java.view.GlobalViewParameter;
import main.java.itu.annotation.Controller;
@WebListener
public class MyServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        HashMap<UrlMethodeHttpMapping,MethodeControllerMapping> mappingUrl = new HashMap<>();
        ServletContext servletContext = servletContextEvent.getServletContext();
        //maka nom package
        String packageName = servletContext.getInitParameter("controllerPackage");
        System.out.println(packageName);
        Loader.getUrlMappingByAnnotation(UrlMapping.class,mappingUrl,packageName,Controller.class);
        if(!mappingUrl.isEmpty()){
            servletContext.setAttribute("mappingUrl", mappingUrl);
            System.out.println(LocalDateTime.now() + " : SCANN DES CLASSES CONTROLLERS ET LEUR METHODES + URL ACHEVEE");
        }
        else{
            System.out.println(LocalDateTime.now() + ":" + "ERREUR LORS DU SCANN");
        }
        GlobalViewParameter.setPrefix(servletContext.getInitParameter("prefix"));
        GlobalViewParameter.setSuffixe(servletContext.getInitParameter("suffix"));
        System.out.println(GlobalViewParameter.getPrefix());
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // ...
    }
}