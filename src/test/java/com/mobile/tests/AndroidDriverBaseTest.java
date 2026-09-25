package com.mobile.tests;

import com.mobile.framework.config.MobileConfig;
import com.mobile.framework.core.device.DeviceManagers;
import com.mobile.framework.core.DriverManager;
import com.mobile.framework.core.Platform;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;

/**
 * Base class for Android UI tests.
 *
 * Configures the Android platform, prepares the application,
 * starts the Appium driver before each test method,
 * and quits the driver after test execution.
 *
 * Test classes should extend this class to reuse
 * the common Android driver lifecycle.
 */
public class AndroidDriverBaseTest {
    @BeforeMethod(alwaysRun = true)
    public void setUp() throws IOException, InterruptedException {
        Platform.setCurrent(Platform.ANDROID);
        if(!DeviceManagers.current().isAppInstalled(MobileConfig.ANDROID_APP_PACKAGE)){
            DeviceManagers.current().installApp(MobileConfig.ANDROID_APK_PATH);
        }

        DriverManager.startDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(){
        DriverManager.quitDriver();
    }
}
