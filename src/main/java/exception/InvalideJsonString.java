package main.java.exception;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class InvalideJsonString extends RuntimeException{
    private String json;


    public InvalideJsonString(String json){
        super("Le json :  '"+ json + "  ' n'est pas valide");
        this.json = json;
    }
    public String getUrl(){
        return json;
    }
    public void setUrl(String json) {
        this.json = json;
    }
   
    
    
}
