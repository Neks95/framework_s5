package main.java.exception;


public class UnauthorizedException extends RuntimeException {

    private final boolean authenticated;

    public UnauthorizedException(boolean authenticated) {
        super(authenticated 
            ? "Accès refusé : droits insuffisants." 
            : "Authentification requise.");
        this.authenticated = authenticated;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}