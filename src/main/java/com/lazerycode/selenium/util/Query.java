package com.lazerycode.selenium.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.HashMap;
import java.util.List;

public class Query {

    private static RemoteWebDriver driver;
    private static String currentBrowserName;

    /**
     * Set a static driver object that wil be used for all instances of Query
     *
     * @param driverObject
     */
    public static void initQueryObjects(RemoteWebDriver driverObject) {
        driver = driverObject;
        if (null != driver) {
            currentBrowserName = driver.getCapabilities().getBrowserName();
        }
    }

    private final By defaultLocator;
    private HashMap<String, By> customLocators = new HashMap<>();

    public Query(By defaultLocator) {
        if (null == defaultLocator) throw new NullPointerException("Query locator cannot be null!");
        this.defaultLocator = defaultLocator;
    }

    /**
     * Specify a alternate locator for a specific browser.
     * <p>
     * Any actions that use a By object will examine the `browserName` capability of the current driver,
     * if it matches what you have specified here this locator will be used instead.
     * <p>
     * It is Suggested you pass in a org.openqa.selenium.remote.BrowserType field to ensure accuracy, example:
     * <p>
     * Query query = newQuery(By.id("foo");
     * query.addAlternateLocator(BrowserType.GOOGLECHROME, By.id("bar");
     * <p>
     * This is intentionally a String for future compatibility.
     *
     * @param browser String value matching a browsername capability
     * @param locator A By object used for locating webElements
     */

    public void addAlternateLocator(String browser, By locator) {
        customLocators.put(browser, locator);
    }

    /**
     * This will return a WebElement object if the supplied locator could find a valid WebElement.
     *
     * @return WebElement
     */
    public WebElement findWebElement() {
        checkDriverIsSet();
        if (customLocators.containsKey(currentBrowserName)) {
            return driver.findElement(customLocators.get(currentBrowserName));
        }

        return driver.findElement(defaultLocator);
    }

    /**
     * This will return a list of WebElement objects, it may be empty if the supplied locator does not match any elements on screen
     *
     * @return List&lt;WebElement>&gt;
     */
    public List<WebElement> findWebElements() {
        checkDriverIsSet();
        if (customLocators.containsKey(currentBrowserName)) {
            return driver.findElements(customLocators.get(currentBrowserName));
        }
        return driver.findElements(defaultLocator);
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
        if (customLocators.containsKey(currentBrowserName)) {
            return customLocators.get(currentBrowserName);
        }
        return defaultLocator;
    }

    private void checkDriverIsSet() {
        if (null == driver) {
            throw new IllegalStateException("Driver object has not been set... You must call 'Query.initQueryObjects(driver);'!");
        }
    }
}