package main.java.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import main.java.core.NkSession;

public class SessionHandler {
     public static NkSession resolveSession(HttpServletRequest req) {
        HttpSession httpSession = req.getSession(true);
        NkSession session = (NkSession) httpSession.getAttribute("nkSession");
        if (session == null) {
            session = new NkSession();
            httpSession.setAttribute("nkSession", session);
        }
        return session;
    }
    
}
