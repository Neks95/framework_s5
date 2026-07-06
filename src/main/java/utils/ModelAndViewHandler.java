package main.java.utils;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.core.ModelAndView;
import main.java.view.GlobalViewParameter;

public final class ModelAndViewHandler {
    public static void processModelAndView(ModelAndView mv,HttpServletRequest req,HttpServletResponse res){
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
            } catch (Exception e)  {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }

    }
    

    
}
