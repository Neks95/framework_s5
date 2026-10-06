package main.java.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.core.MethodeControllerMapping;
import main.java.core.MethodeHttp;
import main.java.core.UrlMethodeHttpMapping;
import main.java.exception.UrlNotFoundException;
import main.java.itu.annotation.ApiRest;
import main.java.utils.Executor;

public class FrontControllerServlet extends HttpServlet {
    private HashMap<UrlMethodeHttpMapping, MethodeControllerMapping> mappingUrl = new HashMap<>();

    public void init() {
        // controllerPackage = this.getInitParameter("controllerPackage");
        // System.out.println("Package = " + controllerPackage);
        // Loader.getUrlMappingByAnnotation(UrlMapping.class,mappingUrl,controllerPackage,Controller.class);
        // System.out.println("Nb mappings = " + mappingUrl.size());
        mappingUrl = (HashMap<UrlMethodeHttpMapping, MethodeControllerMapping>) this.getServletContext()
                .getAttribute("mappingUrl");
        System.out.println("Nb mappings = " + mappingUrl.size());

    }

    public MethodeControllerMapping processPath(String path) {
        return mappingUrl.get(new UrlMethodeHttpMapping(path, MethodeHttp.valueOf("GET")));
    }

    public void processRequest(HttpServletRequest req, HttpServletResponse res)
            throws IOException, UrlNotFoundException {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        // res.setContentType("text/plain");
        PrintWriter out = res.getWriter();

        try {
            MethodeControllerMapping method = processPath(path);
            if (method == null) {
                throw new UrlNotFoundException(path);
            }
            if (method != null && method.isApiRest()) {
                Executor.invokeApiRelatedFuntion(method, req, res);
                return; 
            }
            else {
                Method m = method.getMethod();
                // out.println("URL : " + path);
                // out.println("METHODE : " + m.getName() + " / CONTROLLER : " + m.getDeclaringClass());
                // out.println(method.isApiRest()); 
                Executor.invokeViewRelatedFunction(method, req, res);
            }
        } catch (UrlNotFoundException e) {
            out.println("L'url " + e.getUrl() + " n'est pas mappee a une methode.");
            out.println("Les urls dispo sont : ");
            for (Map.Entry<UrlMethodeHttpMapping, MethodeControllerMapping> entry : mappingUrl.entrySet()) {
                out.println("- " + entry.getKey().getUrl() + "(" + entry.getKey().getMethode() + ") : "
                        + entry.getValue().getClasse());
            }
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

}