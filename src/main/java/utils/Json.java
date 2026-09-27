package main.java.utils;
import com.google.gson.JsonParser;

public class Json {

public static boolean isValidJson(String json) {
    try {
        JsonParser.parseString(json);
        return true;
    } catch (Exception e) {
        return false;
    }
}


    
}
