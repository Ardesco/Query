package com.lazerycode.selenium.util;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobilePlatform;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.RemoteWebDriver;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

public class AssignDriverObjectTest {
    private static final RemoteWebDriver MOCKED_CHROME_DRIVER = mock(ChromeDriver.class);
    private static final AppiumDriver MOCKED_APPIUM_DRIVER = mock(AppiumDriver.class);

    private Query valid;
    private Query empty;

    public AssignDriverObjectTest() {
        Capabilities mockedRemoteWebDriverCapabilities = mock(Capabilities.class);
        when(mockedRemoteWebDriverCapabilities.getBrowserName()).thenReturn(BrowserType.GOOGLECHROME);
        when(mockedRemoteWebDriverCapabilities.getCapability(PLATFORM_NAME)).thenReturn(Platform.YOSEMITE);
        when(MOCKED_CHROME_DRIVER.getCapabilities()).thenReturn(mockedRemoteWebDriverCapabilities);

        Capabilities mockedAppiumDriverCapabilities = mock(Capabilities.class);
        when(mockedAppiumDriverCapabilities.getBrowserName()).thenReturn("");
        when(mockedAppiumDriverCapabilities.getCapability(PLATFORM_NAME)).thenReturn(Platform.fromString(MobilePlatform.ANDROID));
        when(mockedAppiumDriverCapabilities.getCapability("automationName")).thenReturn("Appium");
        when(MOCKED_APPIUM_DRIVER.getCapabilities()).thenReturn(mockedAppiumDriverCapabilities);
    }

    @Test
    public void assignRemoteWebDriver() {
        valid = new Query().defaultLocator(By.id("foo"));
        empty = null;

        assertThat(valid.driverIsSet()).isFalse();

        initQueryObjects(this, MOCKED_CHROME_DRIVER);

        assertThat(empty).isNull();
        assertThat(valid.driverIsSet()).isTrue();
    }

    @Test
    public void assignAppiumDriver() {
        valid = new Query().defaultLocator(By.id("bar"));
        empty = null;

        assertThat(valid.driverIsSet()).isFalse();

        initQueryObjects(this, MOCKED_APPIUM_DRIVER);

        assertThat(empty).isNull();
        assertThat(valid.driverIsSet()).isTrue();
    }

    @Test
    public void assignDriverToClass() {
        SomePageObject somePageObject = new SomePageObject();

        assertThat(somePageObject.element.driverIsSet()).isFalse();
        assertThat(somePageObject.anotherElement.driverIsSet()).isFalse();

        initQueryObjects(somePageObject, MOCKED_CHROME_DRIVER);

        assertThat(somePageObject.element.driverIsSet()).isTrue();
        assertThat(somePageObject.anotherElement.driverIsSet()).isTrue();
    }
}
