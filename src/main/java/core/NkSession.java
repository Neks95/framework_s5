package main.java.core;

import java.util.HashMap;

public class NkSession {
    private HashMap<String,Object> attributes;

    public NkSession() {
        attributes = new HashMap<>();
    }

    public HashMap<String, Object> getAttributes() {
        return attributes;
    }

    public void addAttribute(String nom , Object attribut){
        attributes.put(nom,attribut);
    }

    public void deleteAttribut(String key){
        attributes.remove(key);
    }

    public Object getAttribute(String key){
        return attributes.get(key);
    }

    public void setAttributes(HashMap<String, Object> attributes) {
        this.attributes = attributes;
    }
    
    
}
