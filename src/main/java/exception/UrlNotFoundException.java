package main.java.exception;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class UrlNotFoundException extends RuntimeException{
    private String url;


    public UrlNotFoundException(String url){
        super("L'url "+ url + "n'existe pas(404)");
        this.url = url;
    }
    public String getUrl(){
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
   
    
    
}
