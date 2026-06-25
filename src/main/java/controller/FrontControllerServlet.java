package main.java.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.core.MethodeControllerMapping;
import main.java.core.MethodeHttp;
import main.java.core.UrlMethodeHttpMapping;
import main.java.exception.UrlNotFoundException;
import main.java.itu.annotation.Controller;
import main.java.itu.annotation.UrlMapping;
import main.java.utils.Loader;

public class FrontControllerServlet extends HttpServlet { 
    private List<Class<?>> listController;
    private String controllerPackage;
    private HashMap<UrlMethodeHttpMapping,MethodeControllerMapping> mappingUrl;

    public void init() {
        controllerPackage = this.getInitParameter("controllerPackage");
        System.out.println("Package = " + controllerPackage);

        listController = Loader.getAnnotatedClasses(controllerPackage, Controller.class);
        System.out.println("Nb controllers = " + listController.size());

        mappingUrl = Loader.getUrlMappingByAnnotation(UrlMapping.class, listController);
        System.out.println("Nb mappings = " + mappingUrl.size());
    }

    public MethodeControllerMapping processPath(String path){
        return mappingUrl.get(new UrlMethodeHttpMapping(path, MethodeHttp.valueOf("GET")));
    }

    public void processRequest(HttpServletRequest req , HttpServletResponse res) throws IOException ,UrlNotFoundException{
        String path = req.getRequestURI().substring(req.getContextPath().length());
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        try {
            MethodeControllerMapping method = processPath(path);
            if(method == null){
                throw new UrlNotFoundException(path);
            }
            else{
                Method m = method.getM();
                out.println("URL : " + path);
                out.println("METHODE : "+ m.getName() + " / CONTROLLER : "+ m.getDeclaringClass());
            }
        } catch (UrlNotFoundException e) {
            out.println("L'url " + e.getUrl() + " n'est pas mappee a une methode.");
            out.println("Les urls dispo sont : ");
            for (Map.Entry<UrlMethodeHttpMapping, MethodeControllerMapping> entry : mappingUrl.entrySet()) {
                out.println("- " + entry.getKey().getUrl() + "("+entry.getKey().getMethode() + ") : " + entry.getValue().getClasse());
            }
        } 
    }

    public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException{
        processRequest(req, res);
    }

    public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException{
        processRequest(req, res);
    }

    
}