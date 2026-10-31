package main.java.exception;


public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException() {
        super("Permission insuffisante .");
    }

}