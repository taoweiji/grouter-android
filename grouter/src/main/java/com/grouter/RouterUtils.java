package com.grouter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class RouterUtils {
    static Map<String, Object> objectToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        if (obj instanceof Map) {
            Map tmp = (Map) obj;
            Set keySet = tmp.keySet();
            for (Object key : keySet) {
                Object value = tmp.get(key);
                if (value != null) {
                    map.put(key.toString(), value);
                }
            }
            return map;
        }
        if (obj instanceof List) {
            List list = (List) obj;
            for (int i = 0; i < list.size(); i++) {
                map.put(String.valueOf(i), list.get(i));
            }
            return map;
        }
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            try {
                Object value = field.get(obj);
                if (value != null) {
                    map.put(fieldName, value);
                }
            } catch (IllegalAccessException e) {
                LoggerUtils.handleException(e);
            }
        }
        return map;
    }
}
