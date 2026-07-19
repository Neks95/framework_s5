package main.java.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.core.MethodeControllerMapping;
import main.java.core.ModelAndView;
import main.java.listener.MyServletContextListener;
import main.java.view.GlobalViewParameter;

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

    


}
