Query
========

[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://travis-ci.org/Ardesco/Query.svg?branch=master)](https://travis-ci.org/Ardesco/Query)
[![codecov](https://codecov.io/gh/ardesco/query/branch/master/graph/badge.svg)](https://codecov.io/gh/ardesco/query)
[![GitHub release](https://img.shields.io/github/release/Ardesco/Query/all.svg?colorB=brightgreen)](https://github.com/Ardesco/Query)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.lazerycode.selenium/query/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.lazerycode.selenium/query)
[![Javadocs](http://www.javadoc.io/badge/com.lazerycode.selenium/query.svg)](http://www.javadoc.io/doc/com.lazerycode.selenium/query)

A query object designed to make page objects easier to manage

## How do I get started?

It's nice and simple, add the following entry to your POM:

    <dependency>
        <groupId>com.lazerycode.selenium</groupId>
        <artifactId>query</artifactId>
        <version>2.0.0-BETA1</version>
        <scope>test</scope>
    </dependency>
    
Then just create a basic query object in your page object when you want to use it:

    Query query = new Query().defaultLocator(By.id("foo"));
    
Then when you create your driver object you will need let the Query object know about it like this:
    
    query.usingDriver(driver);
    
Or if you already have a driver object you can just do it all in one go:

    Query query = new Query().defaultLocator(By.id("foo")).usingDriver(driver);    
    
Do you want to have different locators for different browsers?  You can add overrides for different browsers:

    query.addAlternateLocator(BrowserType.GOOGLECHROME, By.id("bar")
       .addAlternateLocator("custom_driver", By.id("custom");
    
Once you have set custom locators the query object will check the desired capabilities of the current instantiated driver and just use the appropriate locator

## Setting a driver object for every Query object is a real PITA, isn't there an easier way?

Instead of passing a `.usingDriver(driver)` command to each driver object you can instead put the following code into your constructor:

    initQueryObjects(this, driver);
    
This will scan the current class for valid Query objects and then assign the supplied driver object to each Query object.  This does need to be an instantiated driver object, passing in a null will result in an error.  You can then of course still modify the driver object assigned to a Query object at any point in the future using the `.usingDriver(driver)` command on individual Query objects.        
    
## OK, I have a query object. Now what?    

It's designed to return certain element types that you can use in your page objects, the various types are shown below:

    WebElement element = query.findWebElement();
    List<WebElement> elementList = query.findWebElements();
    Select selectElement = query.findSelectElement();
    MobileElement mobileElement = query.findMobileElement();
    List<MobileElement> mobileElementList = query.findMobileElements();
    
Ok, that's kind of useful, anything else?

Have you ever got frustrated trying to get locators out of element to use in expected conditions?  No longer a problem:

    WebDriverWait wait = new WebDriverWait(driver, 15, 100);
    wait.until(ExpectedConditions.visibilityOfElementLocated(query.by()));
    
That's all for now, if you can think of any useful additions just raise an issue.    