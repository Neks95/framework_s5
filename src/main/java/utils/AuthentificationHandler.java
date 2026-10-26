package main.java.utils;

import java.lang.reflect.Method;

import main.java.core.NkSession;
import main.java.exception.NotAuthenticatedException;
import main.java.itu.annotation.HasRole;
import main.java.itu.annotation.Secured;

public final class AuthentificationHandler {
    public static  String AUTH_ATTR ;

    private AuthentificationHandler() {}
    
    public static void checkAuth(Method m, NkSession session) throws NotAuthenticatedException{

        boolean needAuth = m.isAnnotationPresent(Secured.class)
                        || m.isAnnotationPresent(HasRole.class);

        if (!needAuth) {
            return;
        }
      
        boolean authenticated = session != null
                && Boolean.TRUE.equals(session.getAttribute(AUTH_ATTR));

        if (!authenticated) {
            throw new NotAuthenticatedException();
        }
    }

    public static void setAUTH_ATTR(String aUTH_ATTR) {
        AUTH_ATTR = aUTH_ATTR;
    }
}