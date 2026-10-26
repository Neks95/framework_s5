package main.java.utils;

import main.java.itu.annotation.HasRole;
import java.lang.reflect.Method;
import main.java.core.NkSession;
import main.java.exception.NotAuthorizedException;

public class AuthorizationHandler {
    private static String PERMISSION_NAME;

    public AuthorizationHandler() {

    }

    public static void checkPermission(Method m, NkSession session) throws NotAuthorizedException {
        AuthentificationHandler.checkAuth(m,session);
        HasRole annotation = m.getAnnotation(HasRole.class);
        if (annotation == null) {
            return;
        }

        Object raw = session.getAttribute(PERMISSION_NAME);
        if (!(raw instanceof String valeurSession)) {
            throw new NotAuthorizedException();
        }

        for (String role : annotation.role()) {
            if (valeurSession.equals(role)) {
                return;
            }
        }
        throw new NotAuthorizedException();

    }

    public static void setPERMISSION_NAME(String pERMISSION_NAME) {
        PERMISSION_NAME = pERMISSION_NAME;
    }

}
