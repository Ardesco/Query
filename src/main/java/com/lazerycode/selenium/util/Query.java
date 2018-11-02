package com.lazerycode.selenium.util;

import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Query {

    private RemoteWebDriver driver;
    private String currentType;
    private By defaultLocator;
    private HashMap<String, By> customLocators = new HashMap<>();
    private boolean isAppiumDriver;

    /**
     * Specify a default locator that will be used if a more specific locator cannot be detected.
     *
     * @param locator
     * @return this
     */
    public Query defaultLocator(By locator) {
        this.defaultLocator = locator;

        return this;
    }

    /**
     * Specify a alternate locator for a specific browser.
     * <p>
     * Any actions that use a By object will examine the `browserName` capability of the current driver,
     * if it matches what you have specified here this locator will be used instead.
     * The browserName check is case insensitive!
     * <p>
     * It is Suggested you pass in a org.openqa.selenium.remote.BrowserType object to ensure accuracy
     * (or if you are using Appium a io.appium.java_client.remote.MobileBrowserType, or io.appium.java_client.remoteMobilePlatform),
     * examples:
     * <p>
     * Query query = newQuery(By.id("foo");
     * query.addSpecificLocator(BrowserType.GOOGLECHROME, By.id("bar");
     * query.addSpecificLocator(MobileBrowserType.BROWSER, By.id("oof");
     * query.addSpecificLocator(MobilePlatform.ANDROID, By.id("rab");
     * <p>
     * This is intentionally a String for future compatibility.
     *
     * @param browser String value matching a BrowserType, MobileBrowserType, or MobilePlatform capability
     * @param locator A By object used for locating webElements
     */

    public Query addSpecificLocator(String browser, By locator) {
        customLocators.put(browser.toUpperCase(), locator);

        return this;
    }

    /**
     * Specify the driver object that will be used to find elements
     *
     * @param driverObject
     * @return this
     */
    public Query usingDriver(RemoteWebDriver driverObject) {
        if (null != driverObject) {
            driver = driverObject;
            Object automationName = driver.getCapabilities().getCapability("automationName");
            isAppiumDriver = (null != automationName) && automationName.toString().toLowerCase().equals("appium");
            currentType = driver.getCapabilities().getBrowserName();
            if (isAppiumDriver && (null == currentType || currentType.isEmpty())) {
                currentType = driver.getCapabilities().getPlatform().toString();
            }
        } else {
            throw new NullPointerException("Driver object is null!");
        }

        return this;
    }

    /**
     * This will return a WebElement object if the supplied locator could find a valid WebElement.
     *
     * @return WebElement
     */
    public WebElement findWebElement() {
        return driver.findElement(locator());
    }

    /**
     * This will return a MobileElement object if the supplied locator could find a valid MobileElement.
     *
     * @return MobileElement
     */
    public MobileElement findMobileElement() {
        if (isAppiumDriver) {
            return (MobileElement) driver.findElement(locator());
        }
        throw new UnsupportedOperationException("You don't seem to be using Appium!");
    }

    /**
     * This will return a list of WebElement objects, it may be empty if the supplied locator does not match any elements on screen
     *
     * @return List&lt;WebElement>&gt;
     */
    public List<WebElement> findWebElements() {
        return driver.findElements(locator());
    }

    /**
     * This will return a list of MobileElement objects, it may be empty if the supplied locator does not match any elements on screen
     *
     * @return List&lt;MobileElement>&gt;
     */
    public List<MobileElement> findMobileElements() {
        if (isAppiumDriver) {
            List<WebElement> elementsFound = driver.findElements(locator());
            List<MobileElement> mobileElementsToReturn = new ArrayList<>();
            for (WebElement element : elementsFound) {
                mobileElementsToReturn.add((MobileElement) element);
            }
            return mobileElementsToReturn;
        }
        throw new UnsupportedOperationException("You don't seem to be using Appium!");
    }

    /**
     * This will return a Select object if the supplied locator could find a valid WebElement.
     *
     * @return Select
     */
    public Select findSelectElement() {
        return new Select(findWebElement());
    }

    /**
     * This will return the locator currently associated with your driver object.
     * This is useful for passing into ExpectedConditions
     *
     * @return By
     */
    public By locator() {
        checkDriverIsSet();
        By locatorToReturn = customLocators.getOrDefault(currentType.toUpperCase(), defaultLocator);

        return checkLocatorIsNotNull(locatorToReturn);
    }

    private By checkLocatorIsNotNull(By locator) {
        if (null == locator) {
            throw new IllegalStateException(String.format("Unable to detect valid locator for '%s' and a default locator has not been set!", currentType));
        }

        return locator;
    }

    private void checkDriverIsSet() {
        if (null == driver) {
            throw new IllegalStateException("Driver object has not been set... You must call 'Query.initQueryObject(driver);'!");
        }
    }
}