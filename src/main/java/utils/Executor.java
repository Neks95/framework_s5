package main.java.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.core.MethodeControllerMapping;
import main.java.core.ModelAndView;
import main.java.view.GlobalViewParameter;

public class Executor {
    public static void invokeViewRelatedFunction(MethodeControllerMapping m,HttpServletRequest req,HttpServletResponse res) {

    try {
       Object controller = m.getClasse()
       .getDeclaredConstructor()
       .newInstance();
       Method method = m.getMethod();

       Object retour = method.invoke(controller);
        if (retour instanceof ModelAndView mv) {
            HashMap<String, Object> data = mv.getAttribute();
            if (data != null) {
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    req.setAttribute(entry.getKey(), entry.getValue());
                }
            }
            String nomPage = GlobalViewParameter.prefix
                    + mv.getNomPage()
                    + GlobalViewParameter.suffixe;
            System.out.println(nomPage);
            try {
                RequestDispatcher dispatcher = req.getRequestDispatcher(nomPage); 
                dispatcher.forward(req, res);
            } catch (ServletException s) {
                System.out.println(s.getMessage());
                throw s;
            }
        }

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}


}
