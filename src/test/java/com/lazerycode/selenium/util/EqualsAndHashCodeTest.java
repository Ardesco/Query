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

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

public class EqualsAndHashCodeTest {

    private static final By DEFAULT_LOCATOR = By.id("foo");
    private static final By ALTERNATE_LOCATOR = By.id("bar");
    private static final WebElement MOCKED_WEB_ELEMENT_FOR_DEFAULT = mock(WebElement.class);
    private static final MobileElement MOCKED_MOBILE_ELEMENT_FOR_DEFAULT = mock(MobileElement.class);
    private static final List<WebElement> MOCKED_WEB_ELEMENT_LIST_FOR_DEFAULT = Collections.singletonList(MOCKED_WEB_ELEMENT_FOR_DEFAULT);
    private static final List<WebElement> MOCKED_MOBILE_ELEMENT_LIST_FOR_DEFAULT = Collections.singletonList(MOCKED_MOBILE_ELEMENT_FOR_DEFAULT);

    @Test
    public void equalsInitial() {
        Query first = new Query();
        Query second = new Query();

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    public void equalsDefault() {
        Query first = new Query().defaultLocator(DEFAULT_LOCATOR);
        Query second = new Query().defaultLocator(DEFAULT_LOCATOR);

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    public void notEqualsDefault() {
        Query first = new Query().defaultLocator(DEFAULT_LOCATOR);
        Query second = new Query().defaultLocator(ALTERNATE_LOCATOR);

        assertThat(first).isNotEqualTo(second);
        assertThat(first.hashCode()).isNotEqualTo(second.hashCode());
    }

    @Test
    public void equalsSpecific() {
        Query first = new Query().addSpecificLocator(BrowserType.GOOGLECHROME, DEFAULT_LOCATOR);
        Query second = new Query().addSpecificLocator(BrowserType.GOOGLECHROME, DEFAULT_LOCATOR);

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    public void notEqualsSpecificBrowser() {
        Query first = new Query().addSpecificLocator(BrowserType.GOOGLECHROME, DEFAULT_LOCATOR);
        Query second = new Query().addSpecificLocator(BrowserType.FIREFOX, DEFAULT_LOCATOR);

        assertThat(first).isNotEqualTo(second);
        assertThat(first.hashCode()).isNotEqualTo(second.hashCode());
    }

    @Test
    public void notEqualsSpecificBy() {
        Query first = new Query().addSpecificLocator(BrowserType.GOOGLECHROME, DEFAULT_LOCATOR);
        Query second = new Query().addSpecificLocator(BrowserType.GOOGLECHROME, ALTERNATE_LOCATOR);

        assertThat(first).isNotEqualTo(second);
        assertThat(first.hashCode()).isNotEqualTo(second.hashCode());
    }

    @Test
    public void equalsDriver() {
        Capabilities mockedCapabilities = mock(Capabilities.class);
        when(mockedCapabilities.getBrowserName()).thenReturn(BrowserType.GOOGLECHROME);
        when(mockedCapabilities.getCapability(PLATFORM_NAME)).thenReturn(Platform.YOSEMITE);

        RemoteWebDriver mockedWebDriver = mock(RemoteWebDriver.class);
        when(mockedWebDriver.getCapabilities()).thenReturn(mockedCapabilities);
        when(mockedWebDriver.findElement(DEFAULT_LOCATOR)).thenReturn(MOCKED_WEB_ELEMENT_FOR_DEFAULT);
        when(mockedWebDriver.findElements(DEFAULT_LOCATOR)).thenReturn(MOCKED_WEB_ELEMENT_LIST_FOR_DEFAULT);

        Query first = new Query().usingDriver(mockedWebDriver);
        Query second = new Query().usingDriver(mockedWebDriver);

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    public void notEqualsDriver() {
        Capabilities mockedWebDriverCapabilities = mock(Capabilities.class);
        when(mockedWebDriverCapabilities.getBrowserName()).thenReturn(BrowserType.GOOGLECHROME);
        when(mockedWebDriverCapabilities.getCapability(PLATFORM_NAME)).thenReturn(Platform.YOSEMITE);

        RemoteWebDriver mockedWebDriver = mock(RemoteWebDriver.class);
        when(mockedWebDriver.getCapabilities()).thenReturn(mockedWebDriverCapabilities);
        when(mockedWebDriver.findElement(DEFAULT_LOCATOR)).thenReturn(MOCKED_WEB_ELEMENT_FOR_DEFAULT);
        when(mockedWebDriver.findElements(DEFAULT_LOCATOR)).thenReturn(MOCKED_WEB_ELEMENT_LIST_FOR_DEFAULT);


        Capabilities mockedAppiumCapabilities = mock(Capabilities.class);
        when(mockedAppiumCapabilities.getBrowserName()).thenReturn("");
        when(mockedAppiumCapabilities.getCapability(PLATFORM_NAME)).thenReturn(Platform.fromString(MobilePlatform.ANDROID));
        when(mockedAppiumCapabilities.getCapability("automationName")).thenReturn("Appium");

        RemoteWebDriver mockedAppiumDriver = mock(AndroidDriver.class);
        when(mockedAppiumDriver.getCapabilities()).thenReturn(mockedAppiumCapabilities);
        when(mockedAppiumDriver.findElement(DEFAULT_LOCATOR)).thenReturn(MOCKED_MOBILE_ELEMENT_FOR_DEFAULT);
        when(mockedAppiumDriver.findElements(DEFAULT_LOCATOR)).thenReturn(MOCKED_MOBILE_ELEMENT_LIST_FOR_DEFAULT);

        Query first = new Query().usingDriver(mockedWebDriver);
        Query second = new Query().usingDriver(mockedAppiumDriver);

        assertThat(first).isNotEqualTo(second);
        assertThat(first.hashCode()).isNotEqualTo(second.hashCode());
    }
}
