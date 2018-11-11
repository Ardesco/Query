package com.lazerycode.selenium.util;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.BrowserType;

public class SomePageObject {

    Query element = new Query().defaultLocator(By.name("foo"));
    Query anotherElement = new Query().addSpecificLocator(BrowserType.GOOGLECHROME, By.name("bar"));

}
