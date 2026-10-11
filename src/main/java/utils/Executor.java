package main.java.utils;

import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.reflect.Method;
import java.lang.reflect.*;

import javax.management.RuntimeErrorException;

import org.springframework.context.ApplicationContext;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.core.MethodeControllerMapping;
import main.java.core.ModelAndView;
import main.java.exception.InvalideJsonString;
import main.java.itu.annotation.ModelAttribute;

public class Executor {
    public static void invokeViewRelatedFunction(MethodeControllerMapping m, HttpServletRequest req,
            HttpServletResponse res) {
        try {
            Object controller = m.getClasse().getDeclaredConstructor().newInstance();
            Method method = m.getMethod();

            Parameter[] parameters = method.getParameters();
            Object[] args = new Object[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                Class<?> paramType = parameter.getType();

                if (parameter.isAnnotationPresent(ModelAttribute.class)) {
                    args[i] = bindObject(paramType, req);
                } else {
                    String value = req.getParameter(parameter.getName());
                    if (value == null) {
                        throw new RuntimeException("Paramètre manquant : " + parameter.getName());
                    }
                    args[i] = convert(value, paramType);
                }
            }

            Object retour = method.invoke(controller, args);
            if (retour instanceof ModelAndView mv) {
                ModelAndViewHandler.processModelAndView(mv, req, res);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object bindObject(Class<?> type, HttpServletRequest req) throws Exception {
        Object obj = type.getDeclaredConstructor().newInstance();
        for (Field f : type.getDeclaredFields()) {
            String value = req.getParameter(f.getName());
            if (value == null)
                continue; 
            f.setAccessible(true);
            f.set(obj, convert(value, f.getType()));
        }
        return obj;
    }

    private static Object convert(String value, Class<?> type) {
        if (type == String.class)
            return value;
        if (type == int.class || type == Integer.class)
            return Integer.parseInt(value);
        if (type == double.class || type == Double.class)
            return Double.parseDouble(value);
        if (type == long.class || type == Long.class)
            return Long.parseLong(value);
        if (type == boolean.class || type == Boolean.class)
            return Boolean.parseBoolean(value);
        throw new IllegalArgumentException("Type non supporté : " + type.getName());
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
