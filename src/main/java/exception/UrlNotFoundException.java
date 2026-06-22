package main.java.exception;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class UrlNotFoundException extends RuntimeException{
    private String url;
    private HashMap<String,Method> map;


    public UrlNotFoundException(String url,HashMap<String,Method> map){
        super("L'url "+ url + "n'existe pas(404)");
        this.url = url;
        this.map = map;
    }
    public String getUrl(){
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public HashMap<String, Method> getMap() {
        return map;
    }
    public void setMap(HashMap<String, Method> map) {
        this.map = map;
    }
    
    
}
