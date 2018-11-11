package com.lazerycode.selenium.util;

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

public class AssignRemoteWebDriverTest {
    private static final RemoteWebDriver MOCKED_CHROME_DRIVER = mock(ChromeDriver.class);

    private Query valid = new Query().defaultLocator(By.id("foo"));
    private Query empty;

    public AssignRemoteWebDriverTest() {
        Capabilities mockedCapabilities = mock(Capabilities.class);
        when(mockedCapabilities.getBrowserName()).thenReturn(BrowserType.GOOGLECHROME);
        when(mockedCapabilities.getCapability(PLATFORM_NAME)).thenReturn(Platform.YOSEMITE);
        when(MOCKED_CHROME_DRIVER.getCapabilities()).thenReturn(mockedCapabilities);

        initQueryObjects(this, MOCKED_CHROME_DRIVER);
    }

    @Test
    public void something() {
        assertThat(empty).isNull();
        assertThat(valid.checkDriverIsSet()).isTrue();
    }
}
