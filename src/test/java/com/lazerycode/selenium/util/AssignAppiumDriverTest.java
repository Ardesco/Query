package com.lazerycode.selenium.util;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobilePlatform;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

public class AssignAppiumDriverTest {
    private static final AppiumDriver MOCKED_APPIUM_DRIVER = mock(AppiumDriver.class);

    private Query valid = new Query().defaultLocator(By.id("foo"));
    private Query empty;

    public AssignAppiumDriverTest() {
        Capabilities mockedCapabilities = mock(Capabilities.class);
        when(mockedCapabilities.getBrowserName()).thenReturn("");
        when(mockedCapabilities.getCapability(PLATFORM_NAME)).thenReturn(Platform.fromString(MobilePlatform.ANDROID));
        when(mockedCapabilities.getCapability("automationName")).thenReturn("Appium");
        when(MOCKED_APPIUM_DRIVER.getCapabilities()).thenReturn(mockedCapabilities);

        initQueryObjects(this, MOCKED_APPIUM_DRIVER);
    }

    @Test
    public void something() {
        assertThat(empty).isNull();
        assertThat(valid.checkDriverIsSet()).isTrue();
    }
}
