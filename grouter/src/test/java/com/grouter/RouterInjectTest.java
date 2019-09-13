package com.grouter;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class RouterInjectTest {

//    @RouterField
//    int[] p11;
//
//    @RouterField
//    float[] p12;
//
//    @RouterField
//    double[] p13;
//
//    @RouterField
//    long[] p14;
//
//    @RouterField
//    boolean[] p5;
//
//    @RouterField
//    float p2;
//    @RouterField
//    Float p3;
//    @RouterField
//    List<Float> list;

//    @RouterField
//    List<?> list2;


//    @RouterField
//    ErrorTask errorTask;
    @RouterField
    ErrorTask[] errorTasks;

    @Test
    public void inject() {
        Object value = null;

        if (new Random().nextBoolean()){
            value = "";
        }else {
            value = new Integer(1);
        }


        if (Number.class.isAssignableFrom(value.getClass())){
            System.out.println("true");
        }else {
            System.out.println("false");
        }


//        Field[] fields = this.getClass().getDeclaredFields();
//        for (Field field : fields) {
//            RouterField routerField = field.getAnnotation(RouterField.class);
//            if (routerField == null) {
//                continue;
//            }
////            GenericArrayType parameterizedType = (GenericArrayType) field.getGenericType();
////            try {
////                Class.forName(parameterizedType.getActualTypeArguments()[0].getTypeName());
////            } catch (ClassNotFoundException e) {
////                e.printStackTrace();
////            }
//
////            System.out.println(parameterizedType.getGenericComponentType().getTypeName());
//            System.out.println(field.getType().getTypeName());
//        }
    }

    @Test
    public void inject1() {
    }
}