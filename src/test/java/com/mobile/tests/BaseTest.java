package com.mobile.tests;

import com.mobile.framework.core.DriverManager;
import com.mobile.framework.core.Platform;
import com.mobile.framework.core.device.DeviceManagers;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;

/**
 * Base class for UI tests on the platform selected for the run (-Pplatform).
 *
 * Installs the application if needed, starts the Appium driver
 * before each test method and quits it after.
 */
public class BaseTest {

    /**
     * Other tests (e.g. framework self-tests) may switch the platform;
     * reset it to the one selected for the run.
     */
    @BeforeClass(alwaysRun = true)
    public void resetPlatform() {
        Platform.setCurrent(Platform.configured());
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws IOException, InterruptedException {
        Platform platform = Platform.current();
        if (!DeviceManagers.current().isAppInstalled(platform.appId())) {
            DeviceManagers.current().installApp(platform.appPath());
        }

        DriverManager.startDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
