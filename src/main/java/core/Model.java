package main.java.core;

import java.util.HashMap;

public class Model {
    private HashMap<String,Object> attribute;
    public Model(){
        attribute = new HashMap<>();
    }
    public HashMap<String, Object> getAttribute() {
        return attribute;
    }
    public void addAttribute(String cle , Object valeur) {
        attribute.put(cle, valeur);
    }

    


    
}
