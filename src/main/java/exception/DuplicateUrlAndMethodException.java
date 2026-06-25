package main.java.exception;

import main.java.core.UrlMethodeHttpMapping;

public class DuplicateUrlAndMethodException extends RuntimeException{
    private UrlMethodeHttpMapping map;
    public DuplicateUrlAndMethodException(UrlMethodeHttpMapping map){
        super("L'url "+map.getUrl() + "(" + map.getMethode() + ") existe deja !");
        this.map = map;
    }

    
}
