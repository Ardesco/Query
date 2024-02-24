package com.lazerycode.selenium.util;

import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

public class Query {

    private RemoteWebDriver driver;
    private String currentType;
    private By defaultLocator;
    private final HashMap<String, By> customLocators = new HashMap<>();
    private boolean isAppiumDriver;

    /**
     * Specify a default locator that will be used if a more specific by cannot be detected.
     *
     * @param locator A By object used for locating webElements
     * @return this
     */
    public Query defaultLocator(By locator) {
        this.defaultLocator = locator;

        return this;
    }

    /**
     * Specify a alternate locator for a specific browser/device type.
     * <p>
     * Any actions that use a By object will examine the `browserName` or `PLATFORM_NAME` capability of the current driver,
     * if it matches what you have specified here this by will be used instead.
     * The browserName/PLATFORM_NAME check is case insensitive!
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
     * @param driverObject A RemoteWebdriverObject or something that extends RemoteWebDriver
     * @return this
     */
    public Query usingDriver(RemoteWebDriver driverObject) {
        if (null != driverObject) {
            driver = driverObject;
            Object automationName = driver.getCapabilities().getCapability("automationName");
            isAppiumDriver = (null != automationName) && automationName.toString().equalsIgnoreCase("appium");
            currentType = driver.getCapabilities().getBrowserName();
            if (isAppiumDriver && (null == currentType || currentType.isEmpty())) {
                currentType = driver.getCapabilities().getCapability(PLATFORM_NAME).toString();
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
    public WebElement find() {
        return findWebElement();
    }

    /**
     * This will return a WebElement object if the supplied locator could find a valid WebElement.
     *
     * @return WebElement
     */
    public WebElement findWebElement() {
        return driver.findElement(by());
    }

    /**
     * This will return a MobileElement object if the supplied locator could find a valid MobileElement.
     *
     * @return MobileElement
     */
    public MobileElement findMobileElement() {
        if (isAppiumDriver) {
            return (MobileElement) driver.findElement(by());
        }
        throw new UnsupportedOperationException("You don't seem to be using Appium!");
    }

    /**
     * This will return a list of WebElement objects, it may be empty if the supplied locator does not match any elements on screen
     *
     * @return List&lt;WebElement>&gt;
     */
    public List<WebElement> findAll() {
        return driver.findElements(by());
    }

    /**
     * This will return a list of WebElement objects, it may be empty if the supplied locator does not match any elements on screen
     *
     * @return List&lt;WebElement>&gt;
     */
    public List<WebElement> findWebElements() {
        return driver.findElements(by());
    }

    /**
     * This will return a list of MobileElement objects, it may be empty if the supplied locator does not match any elements on screen
     *
     * @return List&lt;MobileElement>&gt;
     */
    public List<MobileElement> findMobileElements() {
        if (isAppiumDriver) {
            List<WebElement> elementsFound = driver.findElements(by());
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
     * This will return the By object currently associated with your driver object.
     * This is useful for passing into ExpectedConditions
     *
     * @return By
     */
    public By by() {
        if (!driverIsSet()) {
            throw new IllegalStateException("Driver object has not been set... You must call 'Query.initQueryObject(driver);'!");
        }
        By locatorToReturn = customLocators.getOrDefault(currentType.toUpperCase(), defaultLocator);

        return checkLocatorIsNotNull(locatorToReturn);
    }

    private By checkLocatorIsNotNull(By locator) {
        if (null == locator) {
            throw new IllegalStateException(String.format("Unable to detect valid by for '%s' and a default by has not been set!", currentType));
        }

        return locator;
    }

    boolean driverIsSet() {
        return null != driver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query query = (Query) o;
        return isAppiumDriver == query.isAppiumDriver &&
                Objects.equals(driver, query.driver) &&
                Objects.equals(currentType, query.currentType) &&
                Objects.equals(defaultLocator, query.defaultLocator) &&
                Objects.equals(customLocators, query.customLocators);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driver, currentType, defaultLocator, customLocators, isAppiumDriver);
    }
}