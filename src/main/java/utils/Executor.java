package main.java.utils;

import java.io.PrintWriter;
import java.lang.reflect.*;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.core.MethodeControllerMapping;
import main.java.core.ModelAndView;
import main.java.core.NkSession;
import main.java.exception.InvalideJsonString;
import main.java.exception.NotAuthenticatedException;
import main.java.exception.NotAuthorizedException;
import main.java.itu.annotation.ModelAttribute;

public class Executor {


    public static void invokeViewRelatedFunction(MethodeControllerMapping m,
            HttpServletRequest req, HttpServletResponse res) {
        try {
            Object retour = prepareAndInvoke(m, req, res);
            if (retour instanceof ModelAndView mv) {
                ModelAndViewHandler.processModelAndView(mv, req, res);
            }
        } catch (NotAuthenticatedException e) {
            throw e;
        } catch(NotAuthorizedException e){
            throw e;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void invokeApiRelatedFuntion(MethodeControllerMapping m,
            HttpServletRequest req, HttpServletResponse res) {
        try {
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");

            Object retour = prepareAndInvoke(m, req, res);

            PrintWriter out = res.getWriter();
            if (retour instanceof String str) {
                if (Json.isValidJson(str)) {
                    out.println(str);
                } else {
                    throw new InvalideJsonString(str);
                }
            } else {
                out.print(new Gson().toJson(retour));
            }
            out.flush();

        } catch (NotAuthenticatedException e) {
            throw e;
        } catch(NotAuthorizedException e){
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static Object prepareAndInvoke(MethodeControllerMapping m,
            HttpServletRequest req, HttpServletResponse res) throws Exception {
        Object controller = m.getClasse().getDeclaredConstructor().newInstance();
        Method method = m.getMethod();
        NkSession session = SessionHandler.resolveSession(req);
        AuthentificationHandler.checkAuth(method, session);
        AuthorizationHandler.checkPermission(method, session);
        Object[] args = buildArguments(method, session, req, res);
        return method.invoke(controller, args);
    }

    private static Object[] buildArguments(Method method, NkSession session,
            HttpServletRequest req, HttpServletResponse res) throws Exception {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> paramType = parameter.getType();

            if (parameter.isAnnotationPresent(ModelAttribute.class)) {
                args[i] = Binding.bindObject(paramType, req);
            } else if (paramType == NkSession.class) {
                args[i] = session;
            } else if (paramType == HttpServletRequest.class) {
                args[i] = req;
            } else if (paramType == HttpServletResponse.class) {
                args[i] = res;
            } else {
                String value = req.getParameter(parameter.getName());
                if (value == null) {
                    throw new RuntimeException("Parametre manquant : " + parameter.getName());
                }
                args[i] = Binding.convertPrimitif(value, paramType);
            }
        }
        return args;
    }
}