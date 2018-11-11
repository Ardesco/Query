package com.lazerycode.selenium.util;

import org.openqa.selenium.remote.RemoteWebDriver;

import java.lang.reflect.Field;

public class AssignDriver {
    public static void initQueryObjects(Object object, RemoteWebDriver driver) {
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() == Query.class) {
                field.setAccessible(true);
                try {
                    Query queryObject = (Query) field.get(object);
                    if (null != queryObject) {
                        queryObject.usingDriver(driver);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
