package com.xiaozhanxiang.simplegridview.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * author: dai
 * date:2019/3/5
 * 反射工具类封装
 *反射的步骤
 * 1.获取Class对象   Class.forName()   object.getClass()  Class.getSuperclass()
 * 2.获取类对象  clazz.newInstance()
 * 3.获取方法，或者字段  clazz.getDeclaredMethod()  getDeclaredField(String name)
 * 4.执行方法或者修改得到字段值  method.invoke(object,parameters)
 */
public class ReflexUtils {


    public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes){
        Method method = null;
        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()){
            try {
                method = clazz.getDeclaredMethod(methodName,parameterTypes);
            } catch (Exception e) {
            }
        }
        return method;
    }

    public static Object invokeMethod(Object object, String methodName, Class<?> [] parameterTypes,
                                      Object [] parameters){
        //根据 对象、方法名和对应的方法参数 通过取 Method 对象
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        if (method != null) {
            //抑制Java对方法进行检查,主要是针对私有方法而言
            method.setAccessible(true);
            try {
                //返回方法执行的结果
                return method.invoke(object,parameters);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Field getDeclaredField(Object object, String fieldName){
        Field field = null;
        for ( Class<?> clazz = object.getClass();clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (Exception ignored) {
            }
        }
        return field;
    }

    public static void setFieldValue(Object object, String fieldName, Object value){
        Field field = getDeclaredField(object,fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                field.set(object,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static Object getFieldValue(Object object, String fieldName){
        Field field = getDeclaredField(object,fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                return field.get(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
