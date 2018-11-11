package com.lazerycode.selenium.util;

import org.openqa.selenium.remote.RemoteWebDriver;

import java.lang.reflect.Field;

public class AssignDriver {
    public static void initQueryObjects(Object object, RemoteWebDriver driver) {
        Class<?> clazz = object.getClass();
//        Object obj = null;
        try {
//            obj = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == Query.class) {
                    field.setAccessible(true);
                    Query queryObject = (Query) field.get(object);
                    if (null != queryObject) {
                        queryObject.usingDriver(driver);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
