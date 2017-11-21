Query
========

[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://travis-ci.org/Ardesco/Query.svg?branch=master)](https://travis-ci.org/Ardesco/Query)
[![codecov](https://codecov.io/gh/ardesco/query/branch/master/graph/badge.svg)](https://codecov.io/gh/ardesco/query)
![GitHub release](https://img.shields.io/github/release/ardesco/query.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.lazerycode.selenium/query/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.lazerycode.selenium/query)
[![Javadocs](http://www.javadoc.io/badge/com.lazerycode.selenium/query.svg)](http://www.javadoc.io/doc/com.lazerycode.selenium/query)

A query object designed to make page objects easier to manage

## How do I get started?

It's nice and simple, added the following entry to your POM:

    <dependency>
        <groupId>com.lazerycode.selenium</groupId>
        <artifactId>query</artifactId>
        <version>1.0.0</version>
        <scope>test</scope>
    </dependency>

Then just create a basic query object in your page object:

    Query query = newQuery(By.id("foo");
    
Do you want to have different locators for different browsers?  Once you have set the default locatir you can add overrides for different browsers:

    query.addAlternateLocator(BrowserType.GOOGLECHROME, By.id("bar");
    query.addAlternateLocator("custom_driver", By.id("custom");
    
Once you have set custom locators the query object will check the desired capabilities of the current instantiated driver and just use the appropriate locator    
    
## OK, I have a query object. Now what?    

It's designed to return certain element types that you can use in yoru page objects, the various types are shown below:

    WebElement element = query.findWebElement();
    List<WebElement> elementList = query.findWebElements();
    Select selectElement = query.findSelectElement();
    
Ok, that's kind of useful, anything else?

Have you ever got frustrated trying to get locators out of element to use in expected conditions?  No longer a problem:

    WebDriverWait wait = new WebDriverWait(driver, 15, 100);
    wait.until(ExpectedConditions.visibilityOfElementLocated(query.locator));
    
That's all for now, if you can think of any useful additions just raise an issue.    