package router;

import java.util.HashMap;
import java.util.Map;

public class KotlinTypeUtils {

    private static Map<String, String> javaTypeMap = new HashMap<>();
    private static Map<String, String> kotlinTypeMap = new HashMap<>();

    static {
        javaTypeMap.put("void", "void");
        javaTypeMap.put("Runnable", "Runnable");
        javaTypeMap.put("Class", "Class");

        javaTypeMap.put("int", "int");
        javaTypeMap.put("long", "long");
        javaTypeMap.put("float", "float");
        javaTypeMap.put("double", "double");
        javaTypeMap.put("boolean", "boolean");

        kotlinTypeMap.put("Integer", "Integer");
        kotlinTypeMap.put("Long", "Long");
        kotlinTypeMap.put("Float", "Float");
        kotlinTypeMap.put("Double", "Double");
        kotlinTypeMap.put("Boolean", "Boolean");
        kotlinTypeMap.put("String", "Boolean");


        javaTypeMap.put("List", "java.util.List");
        javaTypeMap.put("Collection", "java.util.Collection");
        javaTypeMap.put("ArrayList", "java.util.ArrayList");
        javaTypeMap.put("HashMap", "java.util.HashMap");
        javaTypeMap.put("Map", "java.util.Map");
        javaTypeMap.put("HashSet", "java.util.HashSet");
        javaTypeMap.put("Queue", "java.util.Queue");
        javaTypeMap.put("Set", "java.util.Set");
        javaTypeMap.put("Stack", "java.util.Stack");
        javaTypeMap.put("TreeMap", "java.util.TreeMap");
        javaTypeMap.put("Vector", "java.util.Vector");
        javaTypeMap.put("LinkedList", "java.util.LinkedList");

        kotlinTypeMap.putAll(javaTypeMap);
        kotlinTypeMap.put("Int", "int");
        kotlinTypeMap.put("Int?", "Integer");
        kotlinTypeMap.put("Long", "long");
        kotlinTypeMap.put("Long?", "Long");
        kotlinTypeMap.put("Float", "float");
        kotlinTypeMap.put("Float?", "Float");
        kotlinTypeMap.put("Double", "double");
        kotlinTypeMap.put("Double?", "Double");
        kotlinTypeMap.put("Boolean", "boolean");
        kotlinTypeMap.put("Boolean?", "Boolean");
        kotlinTypeMap.put("IntArray", "int[]");
        kotlinTypeMap.put("LongArray", "long[]");
        kotlinTypeMap.put("FloatArray", "float[]");
        kotlinTypeMap.put("DoubleArray", "double[]");
        kotlinTypeMap.put("BooleanArray", "boolean[]");
    }


    public static String getTypeString(boolean isKotlin, Map<String, String> importMap, String type) {
        String result = null;
        if (isKotlin) {
            result = kotlinTypeMap.get(type);
        } else {
            result = javaTypeMap.get(type);
        }
        // 处理基本类型及基本类型数组
        if (result != null) {
            return result;
        }
        // 去掉可空符号
        if (type.endsWith("?")) {
            type = type.substring(0, type.length() - 1);
            return getTypeString(isKotlin, importMap, type);
        }
        // 直接从import查找是否存在合适的
        result = importMap.get(type);
        if (result != null) {
            return result;
        }
        // 识别Kotlin数组
        if (type.startsWith("Array<")) {
            String tmp = type.substring(6, type.length() - 1);
            return getTypeString(isKotlin, importMap, tmp) + "[]";
        }
        // 识别Java数组
        if (type.endsWith("[]")) {
            String tmp = type.substring(0, type.length() - 2);
            return getTypeString(isKotlin, importMap, tmp) + "[]";
        }
        // 处理泛型
        if (type.contains("<")) {
            String tmp = type.substring(0, type.indexOf("<"));
            return getTypeString(isKotlin, importMap, tmp);
        }
        return type;
    }
}
