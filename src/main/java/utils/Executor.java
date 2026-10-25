package main.java.utils;

import java.io.PrintWriter;
import java.lang.reflect.*;

import com.google.gson.Gson;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.core.MethodeControllerMapping;
import main.java.core.ModelAndView;
import main.java.core.NkSession;
import main.java.exception.InvalideJsonString;
import main.java.exception.UnauthorizedException;
import main.java.itu.annotation.ModelAttribute;

public class Executor {

    public static void invokeViewRelatedFunction(MethodeControllerMapping m, HttpServletRequest req,
            HttpServletResponse res) {
        try {

            Object controller = m.getClasse().getDeclaredConstructor().newInstance();
            Method method = m.getMethod();
            ServletContext ctx = req.getServletContext();
            NkSession session = (NkSession) ctx.getAttribute("session");
            AuthHandler.checkAuth(method, session);
            Parameter[] parameters = method.getParameters();
            Object[] args = new Object[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                Class<?> paramType = parameter.getType();

                if (parameter.isAnnotationPresent(ModelAttribute.class)) {
                    args[i] = Binding.bindObject(paramType, req);
                } else if (paramType == NkSession.class) {
                    if (session == null) {
                        session = new NkSession();
                        ctx.setAttribute("session", session);
                    }
                    args[i] = session;
                } else {
                    String value = req.getParameter(parameter.getName());
                    if (value == null) {
                        throw new RuntimeException("Parametre manquant : " + parameter.getName());
                    }
                    args[i] = Binding.convertPrimitif(value, paramType);
                }
            }

            Object retour = method.invoke(controller, args);

            if (retour instanceof ModelAndView mv) {
                ModelAndViewHandler.processModelAndView(mv, req, res);
            }
        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void invokeApiRelatedFuntion(MethodeControllerMapping m, HttpServletRequest req,
            HttpServletResponse res) {
        try {
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");

            Object controller = m.getClasse().getDeclaredConstructor().newInstance();
            Object retour = m.getMethod().invoke(controller);
            PrintWriter out = res.getWriter();
            if (retour instanceof String) {
                String str = (String) retour;
                if (Json.isValidJson(str)) {
                    out.println(str);
                } else {
                    throw new InvalideJsonString(str);
                }
            } else {
                String json = new Gson().toJson(retour);
                out.print(json);
            }

            out.flush();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
