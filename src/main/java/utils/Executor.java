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


public class Executor {
    public static void invokeViewRelatedFunction(MethodeControllerMapping m,HttpServletRequest req,HttpServletResponse res) {
    try {
       Object controller = m.getClasse()
       .getDeclaredConstructor()
       .newInstance();
       Method method = m.getMethod();

            ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("springContext");

            Parameter[] parameters = method.getParameters();
            Object[] args = new Object[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                Class<?> type = parameters[i].getType();
                if (ApplicationContext.class.isAssignableFrom(type)) {
                    args[i] = applicationContext;
                } else {
                    args[i] = null;
                }
            }

       Object retour = method.invoke(controller);
        if (retour instanceof ModelAndView mv) {
            ModelAndViewHandler.processModelAndView(mv,req,res);
        }
        

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

public static void invokeApiRelatedFuntion(MethodeControllerMapping m, HttpServletRequest req, HttpServletResponse res) {
    try {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        Object controller = m.getClasse().getDeclaredConstructor().newInstance();
        Object retour = m.getMethod().invoke(controller);

        String json = new Gson().toJson(retour);

        PrintWriter out = res.getWriter();
        out.print(json);
        out.flush();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}


    


}
