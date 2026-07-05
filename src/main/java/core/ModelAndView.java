package main.java.core;

import java.util.HashMap;

public class ModelAndView {
    private HashMap<String,Object> attribute;
    private String nomPage;
    public ModelAndView(HashMap<String, Object> attribute, String nomPage) {
        this.attribute = attribute;
        this.nomPage = nomPage;
    }
    public ModelAndView(){
        this.attribute = new HashMap<>();
    }
    public HashMap<String, Object> getAttribute() {
        return attribute;
    }
    public void setAttribute(HashMap<String, Object> attribute) {
        this.attribute = attribute;
    }
    public String getNomPage() {
        return nomPage;
    }
    public void setNomPage(String nomPage) {
        this.nomPage = nomPage;
    }

    public void addAttribue(String nomAttribut , Object data){
        attribute.put(nomAttribut, data);
    }


    
    
}
