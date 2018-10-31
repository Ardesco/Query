package com.lazerycode.selenium.util;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobilePlatform;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QueryTest {

    private static final By DEFAULT_LOCATOR = By.id("foo");
    private static final By CHROME_LOCATOR = By.id("bar");
    private static final By FIREFOX_LOCATOR = By.id("fire");
    private static final WebElement MOCKED_WEB_ELEMENT_FOR_DEFAULT = mock(WebElement.class);
    private static final MobileElement MOCKED_MOBILE_ELEMENT_FOR_DEFAULT = mock(MobileElement.class);
    private static final List<WebElement> MOCKED_WEB_ELEMENT_LIST_FOR_DEFAULT = Collections.singletonList(MOCKED_WEB_ELEMENT_FOR_DEFAULT);
    private static final List<WebElement> MOCKED_MOBILE_ELEMENT_LIST_FOR_DEFAULT = Collections.singletonList(MOCKED_MOBILE_ELEMENT_FOR_DEFAULT);

    @Test(expected = NullPointerException.class)
    public void throwsErrorIfDriverIsNull() {
        new Query().usingDriver(null);
    }

    @Test(expected = IllegalStateException.class)
    public void throwsIllegalStateExceptionWhenCallingLocatorWithNoInit() {
        Query query = new Query().defaultLocator(DEFAULT_LOCATOR);
        query.locator();
    }

    @Test(expected = IllegalStateException.class)
    public void throwsIllegalStateExceptionWhenCallingFindWebElementWithNoInit() {
        Query query = new Query().defaultLocator(DEFAULT_LOCATOR);
        query.findWebElement();
    }

    @Test(expected = IllegalStateException.class)
    public void throwsIllegalStateExceptionWhenCallingFindWebElementsWithNoInit() {
        Query query = new Query().defaultLocator(DEFAULT_LOCATOR);
        query.findWebElements();
    }

    @Test(expected = IllegalStateException.class)
    public void throwsIllegalStateExceptionWhenCallingFindSelectElementWithNoInit() {
        Query query = new Query().defaultLocator(DEFAULT_LOCATOR);
        query.findSelectElement();
    }

    @Test
    public void returnsAValidSelectElement() {
        when(MOCKED_WEB_ELEMENT_FOR_DEFAULT.getTagName()).thenReturn("select");

        Query query = new Query().defaultLocator(DEFAULT_LOCATOR);
        initQueryObject(query);
        Select element = query.findSelectElement();

        assertThat(element).isInstanceOf(Select.class);
    }

    @Test
    public void returnsDefaultLocator() {

        Query query = new Query().defaultLocator(DEFAULT_LOCATOR);
        initQueryObject(query);

        assertThat(query.locator()).isEqualTo(DEFAULT_LOCATOR);
    }

    @Test
    public void returnsChromeLocatorIfSet() {

        Query query = new Query().defaultLocator(DEFAULT_LOCATOR);
        initQueryObject(query);
        query.addAlternateLocator(BrowserType.GOOGLECHROME, CHROME_LOCATOR);

        assertThat(query.locator()).isEqualTo(CHROME_LOCATOR);
    }

    @Test
    public void returnsDefaultLocatorIfDifferentBrowserIsSet() {

        Query query = new Query().defaultLocator(DEFAULT_LOCATOR);
        initQueryObject(query);
        query.addAlternateLocator(BrowserType.FIREFOX, FIREFOX_LOCATOR);

        assertThat(query.locator()).isEqualTo(DEFAULT_LOCATOR);
    }

    @Test
    public void returnsWebElement() {

        Query query = new Query().defaultLocator(DEFAULT_LOCATOR);
        initQueryObject(query);
        WebElement element = query.findWebElement();

        assertThat(element).isEqualTo(MOCKED_WEB_ELEMENT_FOR_DEFAULT);
    }

    @Test
    public void returnsWebElementList() {

        Query query = new Query().defaultLocator(DEFAULT_LOCATOR);
        initQueryObject(query);
        List<WebElement> element = query.findWebElements();

        assertThat(element).isEqualTo(MOCKED_WEB_ELEMENT_LIST_FOR_DEFAULT);
    }

    @Test
    public void returnsMobileElement() {

        Query query = new Query().defaultLocator(DEFAULT_LOCATOR);
        initQueryObjectWithAppiumAndroid(query);
        MobileElement element = query.findMobileElement();

        assertThat(element).isEqualTo(MOCKED_MOBILE_ELEMENT_FOR_DEFAULT);
    }

    @Test
    public void returnsMobileElementList() {

        Query query = new Query().defaultLocator(DEFAULT_LOCATOR);
        initQueryObjectWithAppiumAndroid(query);
        List<MobileElement> element = query.findMobileElements();

        assertThat(element).isEqualTo(MOCKED_MOBILE_ELEMENT_LIST_FOR_DEFAULT);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void findMobileElementThrowsUnsupportedOperationExceptionIfPlatformIsNotAMobileType() {

        Query query = new Query().defaultLocator(DEFAULT_LOCATOR);
        initQueryObject(query);
        query.findMobileElement();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void findMobileElementThrowsUnsupportedOperationExceptionIfPlatformIsAGenericName() {

        Query query = new Query().defaultLocator(DEFAULT_LOCATOR);
        initQueryObject(query);
        query.findMobileElement();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void findMobileElementsThrowsUnsupportedOperationExceptionIfPlatformIsNotAMobileType() {

        Query query = new Query().defaultLocator(DEFAULT_LOCATOR);
        initQueryWithGenericAutomationNameObject(query);
        query.findMobileElements();
    }

    private void initQueryObject(Query queryObject) {
        Capabilities mockedCapabilities = mock(Capabilities.class);
        when(mockedCapabilities.getBrowserName()).thenReturn(BrowserType.GOOGLECHROME);
        when(mockedCapabilities.getPlatform()).thenReturn(Platform.YOSEMITE);

        RemoteWebDriver mockedWebDriver = mock(RemoteWebDriver.class);
        when(mockedWebDriver.getCapabilities()).thenReturn(mockedCapabilities);
        when(mockedWebDriver.findElement(DEFAULT_LOCATOR)).thenReturn(MOCKED_WEB_ELEMENT_FOR_DEFAULT);
        when(mockedWebDriver.findElements(DEFAULT_LOCATOR)).thenReturn(MOCKED_WEB_ELEMENT_LIST_FOR_DEFAULT);

        queryObject.usingDriver(mockedWebDriver);
    }

    private void initQueryObjectWithAppiumAndroid(Query queryObject) {
        Capabilities mockedCapabilities = mock(Capabilities.class);
        when(mockedCapabilities.getBrowserName()).thenReturn("");
        when(mockedCapabilities.getPlatform()).thenReturn(Platform.fromString(MobilePlatform.ANDROID));
        when(mockedCapabilities.getCapability("automationName")).thenReturn("Appium");

        RemoteWebDriver mockedWebDriver = mock(AndroidDriver.class);
        when(mockedWebDriver.getCapabilities()).thenReturn(mockedCapabilities);
        when(mockedWebDriver.findElement(DEFAULT_LOCATOR)).thenReturn(MOCKED_MOBILE_ELEMENT_FOR_DEFAULT);
        when(mockedWebDriver.findElements(DEFAULT_LOCATOR)).thenReturn(MOCKED_MOBILE_ELEMENT_LIST_FOR_DEFAULT);

        queryObject.usingDriver(mockedWebDriver);
    }

    private void initQueryWithGenericAutomationNameObject(Query queryObject) {
        Capabilities mockedCapabilities = mock(Capabilities.class);
        when(mockedCapabilities.getBrowserName()).thenReturn(BrowserType.GOOGLECHROME);
        when(mockedCapabilities.getPlatform()).thenReturn(Platform.YOSEMITE);
        when(mockedCapabilities.getCapability("automationName")).thenReturn("Generic");

        RemoteWebDriver mockedWebDriver = mock(RemoteWebDriver.class);
        when(mockedWebDriver.getCapabilities()).thenReturn(mockedCapabilities);
        when(mockedWebDriver.findElement(DEFAULT_LOCATOR)).thenReturn(MOCKED_WEB_ELEMENT_FOR_DEFAULT);
        when(mockedWebDriver.findElements(DEFAULT_LOCATOR)).thenReturn(MOCKED_WEB_ELEMENT_LIST_FOR_DEFAULT);

        queryObject.usingDriver(mockedWebDriver);
    }

    @Test(expected = IllegalStateException.class)
    public void throwsIllegalStateExceptionIfDefaultLocatorIsNull() {
        Capabilities mockedCapabilities = mock(Capabilities.class);
        when(mockedCapabilities.getBrowserName()).thenReturn(BrowserType.GOOGLECHROME);
        when(mockedCapabilities.getPlatform()).thenReturn(Platform.YOSEMITE);
        when(mockedCapabilities.getCapability("automationName")).thenReturn("Generic");

        RemoteWebDriver mockedWebDriver = mock(RemoteWebDriver.class);
        when(mockedWebDriver.getCapabilities()).thenReturn(mockedCapabilities);

        Query query = new Query().usingDriver(mockedWebDriver);
        query.locator();
    }

    @Test(expected = IllegalStateException.class)
    public void throwsIllegalStateExceptionIfDefaultLocatorIsNullAndCurrentTypeDoesNotMatch() {
        Capabilities mockedCapabilities = mock(Capabilities.class);
        when(mockedCapabilities.getBrowserName()).thenReturn(BrowserType.GOOGLECHROME);
        when(mockedCapabilities.getPlatform()).thenReturn(Platform.YOSEMITE);
        when(mockedCapabilities.getCapability("automationName")).thenReturn("Generic");

        RemoteWebDriver mockedWebDriver = mock(RemoteWebDriver.class);
        when(mockedWebDriver.getCapabilities()).thenReturn(mockedCapabilities);

        Query query = new Query().addAlternateLocator(BrowserType.FIREFOX, FIREFOX_LOCATOR).usingDriver(mockedWebDriver);
        query.locator();
    }

    @Test
    public void doesNotThrowIllegalStateExceptionIfDefaultLocatorIsNullAndCurrentTypeDoesMatch() {
        Capabilities mockedCapabilities = mock(Capabilities.class);
        when(mockedCapabilities.getBrowserName()).thenReturn(BrowserType.GOOGLECHROME);
        when(mockedCapabilities.getPlatform()).thenReturn(Platform.YOSEMITE);
        when(mockedCapabilities.getCapability("automationName")).thenReturn("Generic");

        RemoteWebDriver mockedWebDriver = mock(RemoteWebDriver.class);
        when(mockedWebDriver.getCapabilities()).thenReturn(mockedCapabilities);

        Query query = new Query().addAlternateLocator(BrowserType.GOOGLECHROME, CHROME_LOCATOR).usingDriver(mockedWebDriver);
        query.locator();
    }
}
