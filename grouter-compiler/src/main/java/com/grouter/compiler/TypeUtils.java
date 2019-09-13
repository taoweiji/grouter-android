package com.grouter.compiler;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.HashSet;
import java.util.Set;

public class TypeUtils {
    private static Set<TypeName> baseTypeNameSet = new HashSet<>();
    private static ClassName Context = ClassName.get("android.content", "Context");
    private static ClassName Activity = ClassName.get("android.app", "Activity");
    private static ClassName Application = ClassName.get("android.app", "Application");

    private static ClassName Map = ClassName.get("java.util", "Map");
    private static ClassName HashMap = ClassName.get("java.util", "HashMap");
    private static ClassName List = ClassName.get("java.util", "List");
    private static ClassName ArrayList = ClassName.get("java.util", "ArrayList");
    private static ClassName Runnable = ClassName.get("java.util", "Runnable");
    private static ClassName Class = ClassName.get("java.util", "Class");


    static {
        // 基本类型
        baseTypeNameSet.add(TypeName.VOID);
        baseTypeNameSet.add(TypeName.BOOLEAN);
        baseTypeNameSet.add(TypeName.INT);
        baseTypeNameSet.add(TypeName.LONG);
        baseTypeNameSet.add(TypeName.DOUBLE);
        baseTypeNameSet.add(TypeName.FLOAT);
        // 数组
        baseTypeNameSet.add(ArrayTypeName.of(TypeName.BOOLEAN));
        baseTypeNameSet.add(ArrayTypeName.of(TypeName.INT));
        baseTypeNameSet.add(ArrayTypeName.of(TypeName.LONG));
        baseTypeNameSet.add(ArrayTypeName.of(TypeName.DOUBLE));
        baseTypeNameSet.add(ArrayTypeName.of(TypeName.FLOAT));
        baseTypeNameSet.add(ArrayTypeName.of(TypeName.get(String.class)));

        baseTypeNameSet.add(TypeName.get(Integer.class));
        baseTypeNameSet.add(TypeName.get(Long.class));
        baseTypeNameSet.add(TypeName.get(Double.class));
        baseTypeNameSet.add(TypeName.get(Float.class));
        baseTypeNameSet.add(TypeName.get(Boolean.class));
        baseTypeNameSet.add(TypeName.get(String.class));
        // 基础对象
        baseTypeNameSet.add(Context);
        baseTypeNameSet.add(Activity);
        baseTypeNameSet.add(Application);
        baseTypeNameSet.add(Map);
        baseTypeNameSet.add(HashMap);
        baseTypeNameSet.add(List);
        baseTypeNameSet.add(ArrayList);
        baseTypeNameSet.add(Runnable);
        baseTypeNameSet.add(Class);
    }


    /**
     * @return int/long/float/double/boolean/
     * Integer/Long/Double/Float/Boolean/String/
     * int[]/long[]/float[]/double[]/boolean[]/Object[]/String[]
     * Activity/Context/Application/Object/Map/HashMap/List/ArrayList
     */
    public static String getRouterActivityTypeString(TypeName typeName) {
        // 判断是否泛型
        if (typeName instanceof ParameterizedTypeName) {
            typeName = ((ParameterizedTypeName) typeName).rawType;
        }
        // 获取支持支持的类型
        if (baseTypeNameSet.contains(typeName)) {
            return typeName.toString();
        }
        // 判断是否数组
        else if (typeName instanceof ArrayTypeName) {
            return "Object[]";
        } else {
            return "Object";
        }
    }

    public static String getRouterTaskTypeString(TypeName typeName) {
        // 判断是否泛型
        if (typeName instanceof ParameterizedTypeName) {
            typeName = ((ParameterizedTypeName) typeName).rawType;
        }
        // 获取支持支持的类型
        if (baseTypeNameSet.contains(typeName)) {
            return typeName.toString();
        }
        // 判断是否数组
        else if (typeName instanceof ArrayTypeName) {
            typeName = ((ArrayTypeName) typeName).componentType;
            return getRouterTaskTypeString(typeName) + "[]";
        } else {
            String typeNameString = typeName.toString();
            if (typeNameString.startsWith("android") || typeNameString.startsWith("java")) {
                return typeNameString;
            }
            return "Object";
        }
    }


    public static TypeName getTypeNameFull(String name) {
        if (name.contains("<")) {
//            int index = name.indexOf("<");
            name = name.substring(0, name.indexOf("<"));
//            if (name.contains(",")) {
//                return className;
//            }
//            return ParameterizedTypeName.get(className, getTypeName(name.substring(index + 1, name.length() - 1)));
        }
        return getTypeName(name);
    }

    public static TypeName getTypeName(String name) {
        switch (name) {
            case "void":
                return TypeName.VOID;
            case "int":
                return TypeName.get(int.class);
            case "int[]":
                return TypeName.get(int[].class);
            case "long":
                return TypeName.get(long.class);
            case "long[]":
                return TypeName.get(long[].class);
            case "double":
                return TypeName.get(double.class);
            case "double[]":
                return TypeName.get(double[].class);
            case "float":
                return TypeName.get(float.class);
            case "float[]":
                return TypeName.get(float[].class);
            case "boolean":
                return TypeName.get(boolean.class);
            case "boolean[]":
                return TypeName.get(boolean[].class);
            case "short":
                return TypeName.get(short.class);
            case "short[]":
                return TypeName.get(short[].class);
            case "char":
                return TypeName.get(char.class);
            case "char[]":
                return TypeName.get(char[].class);
            default:
                if (name.endsWith("[]")) {
                    return ArrayTypeName.of(ClassName.bestGuess(name.substring(0, name.length() - 2)));
                }
                return ClassName.bestGuess(name);
        }
    }

    public static String reflectionName(ClassName className) {
        // trivial case: no nested names
//        if (className.names.size() == 2) {
//            String packageName = packageName();
//            if (packageName.isEmpty()) {
//                return className.simpleName();
//            }
//            return packageName + "." + className.simpleName();
//        }
//        // concat top level class name and nested names
//        StringBuilder builder = new StringBuilder();
//        builder.append(className.topLevelClassName());
//        for (String name : className.simpleNames().subList(1, className.simpleNames().size())) {
//            builder.append('$').append(name);
//        }
        return className.packageName() + "." + className.simpleName();
    }
}
