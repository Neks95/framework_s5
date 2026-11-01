package main.java.core;

import java.util.HashMap;

public class ControllerInstance {

    public static  HashMap<String,Object> instanceController = new HashMap<>();

    public ControllerInstance() {
    }

    public static HashMap<String, Object> getInstanceController() {
        return instanceController;
    }

    public static Object getUniqueInstanceFromControllerName(String nom){
        return instanceController.get(nom);
    }

    public static void setInstanceController(HashMap<String, Object> instanceController) {
        ControllerInstance.instanceController = instanceController;
    }
    
    
}
