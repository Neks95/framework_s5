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
import main.java.exception.UrlNotFoundException;
import main.java.itu.annotation.Controller;
import main.java.itu.annotation.UrlMapping;
import main.java.utils.Loader;

public class FrontControllerServlet extends HttpServlet { 
    private List<Class<?>> listController;
    private String controllerPackage;
    private HashMap<String,Method> mappingUrlMethod;

    public void init(){
        controllerPackage = this.getInitParameter("controllerPackage");
        listController = Loader.getAnnotatedClasses(controllerPackage,Controller.class,ElementType.TYPE);
        mappingUrlMethod = Loader.getMethodByAnnotation(UrlMapping.class, listController);
    }

    public Method processPath(String path){
        return mappingUrlMethod.get(path);
    }

    public void processRequest(HttpServletRequest req , HttpServletResponse res) throws IOException ,UrlNotFoundException{
        String path = req.getRequestURI().substring(req.getContextPath().length());
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        try {
            Method m = processPath(path);
            if(m == null){
                throw new UrlNotFoundException(path, mappingUrlMethod);
            }
            else{
                out.print("URL : " + path);
                out.println("METHODE : "+ m.getName() + " / CONTROLLER : "+ m.getDeclaringClass());
            }
        } catch (UrlNotFoundException e) {
            out.println("L'url " + e.getUrl() + " n'est pas mappee a une methode.");
            out.println("Les urls dispo sont : ");
            for (Map.Entry<String, Method> entry : e.getMap().entrySet()) {
                out.println("- " + entry.getKey() + " : " + entry.getValue().getDeclaringClass().getName());
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