package main.java.utils;

import java.lang.reflect.Method;

import main.java.core.NkSession;
import main.java.exception.UnauthorizedException;
import main.java.itu.annotation.Secured;

public final class AuthHandler {

    public static  String AUTH_ATTR ;

    private AuthHandler() {}

    
    public static void checkAuth(Method m, NkSession session) throws UnauthorizedException{
        if (!m.isAnnotationPresent(Secured.class)) {
            return;
        }

        boolean authenticated = session != null
                && Boolean.TRUE.equals(session.getAttribute(AUTH_ATTR));

        if (!authenticated) {
            throw new UnauthorizedException(false);
        }
    }


    public static void setAUTH_ATTR(String aUTH_ATTR) {
        AUTH_ATTR = aUTH_ATTR;
    }
}