package main.java.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontControllerServlet extends HttpServlet { 

    public void processRequest(HttpServletRequest req , HttpServletResponse res) throws IOException{
        String url = req.getRequestURI();
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        out.println(url);
    }

    public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException{
        processRequest(req, res);
    }

    public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    
}