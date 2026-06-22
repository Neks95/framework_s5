package main.java.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    public void processRequest(HttpServletRequest req , HttpServletResponse res) throws IOException{
        String path = req.getContextPath();
        Method m = processPath(path);
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        out.println("Liste des controller");
        for(int i =0;i<listController.size();i++){
            out.println(listController.get(i).getName());
        }
        out.println(m.getName());
    }

    public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException{
        processRequest(req, res);
    }

    public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    
}