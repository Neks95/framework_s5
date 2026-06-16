package main.java.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.itu.annotation.Controller;
import main.java.utils.Loader;

public class FrontControllerServlet extends HttpServlet { 
    private List<String> listController;
    private String controllerPackage;

    public void init(){
        controllerPackage = this.getInitParameter("controllerPackage");
        listController = Loader.getAnnotatedClasses(controllerPackage,Controller.class);
    }

    public void processRequest(HttpServletRequest req , HttpServletResponse res) throws IOException{
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        for(int i =0;i<listController.size();i++){
            out.println(listController.get(i));
        }
    }

    public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException{
        processRequest(req, res);
    }

    public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    
}