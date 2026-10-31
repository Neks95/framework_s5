package main.java.exception;


public class NotAuthenticatedException extends RuntimeException {
    public NotAuthenticatedException() {
        super("Authentification requise.");
    }

}