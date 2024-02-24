package com.lazerycode.selenium.util;

import org.openqa.selenium.By;

public class QueryWrappers {
    public static Query baseQuery(By defaultLocator){
        return new Query().defaultLocator(defaultLocator);
    }

}
