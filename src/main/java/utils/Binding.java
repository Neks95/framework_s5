package main.java.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import main.java.core.MapFile;
import java.lang.reflect.*;


public class Binding {
    public static Object bindObject(Class<?> type, HttpServletRequest req) throws Exception {
    Object obj = type.getDeclaredConstructor().newInstance();
        for (Field f : type.getDeclaredFields()) {
            f.setAccessible(true);

            if (f.getType() == MapFile.class) {
                Part part = req.getPart(f.getName());
                if (part == null || part.getSize() == 0) continue;
                f.set(obj, MapFile.toMapFile(part));
                continue;
            }

            String value = req.getParameter(f.getName());
            if (value == null) continue;
            f.set(obj, convertPrimitif(value, f.getType()));
        }
        return obj;
    }

        public static Object convertPrimitif(String value, Class<?> type) {
        if (type == String.class)
            return value;
        if (type == int.class || type == Integer.class)
            return Integer.parseInt(value);
        if (type == double.class || type == Double.class)
            return Double.parseDouble(value);
        if (type == long.class || type == Long.class)
            return Long.parseLong(value);
        if (type == boolean.class || type == Boolean.class)
            return Boolean.parseBoolean(value);
        throw new IllegalArgumentException("Type non supporté : " + type.getName());
    }

    
}
