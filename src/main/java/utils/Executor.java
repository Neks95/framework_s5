package main.java.utils;

import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.management.RuntimeErrorException;

import org.springframework.context.ApplicationContext;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.core.MethodeControllerMapping;
import main.java.core.ModelAndView;
import main.java.exception.InvalideJsonString;

public class Executor {
    public static void invokeViewRelatedFunction(MethodeControllerMapping m, HttpServletRequest req,
            HttpServletResponse res) {
        try {
            Object controller = m.getClasse()
                    .getDeclaredConstructor()
                    .newInstance();
            Method method = m.getMethod();
            // ApplicationContext applicationContext = (ApplicationContext) req.getServletContext()
            //         .getAttribute("springContext");

            Parameter[] parameters = method.getParameters();
            Object[] args = new Object[parameters.length];

            for(int i = 0 ; i<parameters.length;i++){
                Parameter parameter = parameters[i];
                String paramName = parameter.getName();
                System.out.println(paramName);
                Class<?> paramType = parameter.getType();
                String value = req.getParameter(paramName);
                System.out.println(value);
                if(paramType == String.class){
                    args[i] = value;
                }
                if(paramType == int.class){
                    args[i] = Integer.parseInt(value);
                }
                if(paramType == double.class){
                    args[i] = Double.parseDouble(value);
                }
            }

            // for (int i = 0; i < parameters.length; i++) {
            //     Class<?> type = parameters[i].getType();
            //     if (ApplicationContext.class.isAssignableFrom(type)) {
            //         args[i] = applicationContext;
            //     } else {
            //         args[i] = null;
            //     }
            // }
            Object retour = method.invoke(controller,args);
            if (retour instanceof ModelAndView mv) {
                ModelAndViewHandler.processModelAndView(mv, req, res);
            }
        
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
