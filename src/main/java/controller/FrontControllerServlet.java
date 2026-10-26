package main.java.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.core.MethodeControllerMapping;
import main.java.core.MethodeHttp;
import main.java.core.UrlMethodeHttpMapping;
import main.java.exception.NotAuthenticatedException;
import main.java.exception.NotAuthorizedException;
import main.java.exception.UrlNotFoundException;
import main.java.utils.Executor;


@MultipartConfig
public class FrontControllerServlet extends HttpServlet {
    private HashMap<UrlMethodeHttpMapping, MethodeControllerMapping> mappingUrl = new HashMap<>();

    public void init() {
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
                    PrintWriter out = res.getWriter();

            out.println("L'url " + e.getUrl() + " n'est pas mappee a une methode.");
            out.println("Les urls dispo sont : ");
            for (Map.Entry<UrlMethodeHttpMapping, MethodeControllerMapping> entry : mappingUrl.entrySet()) {
                out.println("- " + entry.getKey().getUrl() + "(" + entry.getKey().getMethode() + ") : "
                        + entry.getValue().getClasse());
            }
        } catch(NotAuthenticatedException e){
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED , e.getMessage());
        }
        catch(NotAuthorizedException e){
            res.sendError(HttpServletResponse.SC_FORBIDDEN , e.getMessage());
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

}