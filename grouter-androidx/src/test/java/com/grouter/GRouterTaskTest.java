//package com.grouter;
//
//import org.junit.Test;
//
//import java.lang.reflect.ParameterizedType;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//public class GRouterTaskTest {
//    Object value;
//
//    public <T> List<T> getList(Class<T> clazz) {
//        if (value == null) {
//            return new ArrayList<>();
//        }
////        if (GRouter.getSerialization() == null) {
////            return null;
////        }
//        try {
//            if (List.class.isAssignableFrom(value.getClass())) {
//                List list = (List) value;
//                if (list.size() == 0) {
//                    return list;
//                }
//                Object first = list.get(0);
//                if (clazz.isAssignableFrom(first.getClass())) {
//                    return (List<T>) value;
//                }
//            } else {
//                throw new UnsupportedOperationException("value is " + value.getClass().getName());
//            }
//            System.out.println("反序列化");
//            return GRouter.getSerialization().deserializeList("", clazz);
//        } catch (Exception e) {
//            e.printStackTrace();
//            LoggerUtils.taskInfo(e);
//        }
//        return new ArrayList<>();
//    }
//
//    @Test
//    public void execute() {
//        value = 1;
////        value = Arrays.asList(new Runnable() {
////            @Override
////            public void run() {
////
////            }
////        }, new Thread());
//
////        List<Thread> result = getList(Thread.class);
////
////        for (Thread item : result) {
////            System.out.println(item.getClass());
////            System.out.println(item);
////        }
//
//    }
//}