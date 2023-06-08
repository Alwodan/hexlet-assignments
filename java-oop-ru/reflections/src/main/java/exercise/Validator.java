package exercise;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// BEGIN
public class Validator {
    public static List<String> validate(Object obj) {
        List<String> result = new ArrayList<>();
        Class<?> clas = obj.getClass();
        for (Field field : clas.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.isAnnotationPresent(NotNull.class) && field.get(obj) == null) {
                    result.add(field.getName());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static Map<String, List<String>> advancedValidate(Object obj) throws IllegalAccessException {
        Map<String, List<String>> result = new HashMap<>();
        Class<?> clas = obj.getClass();
        for (Field field : clas.getDeclaredFields()) {
            field.setAccessible(true);
            List<String> errors = new ArrayList<>();
            if (field.isAnnotationPresent(NotNull.class) && field.get(obj) == null) {
                errors.add("can not be null");
            }
            if (field.isAnnotationPresent(MinLength.class)) {
                MinLength aboba = field.getAnnotation(MinLength.class);
                if (field.get(obj).toString().length() < aboba.minLength()) {
                    errors.add("length less than " + aboba.minLength());
                }
            }
            if (!errors.isEmpty()) {
                result.put(field.getName(), errors);
            }
        }
        return result;
    }
}
// END
