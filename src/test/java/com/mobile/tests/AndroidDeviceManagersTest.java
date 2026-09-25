package com.mobile.tests;

import com.mobile.framework.config.MobileConfig;
import com.mobile.framework.core.device.DeviceManagers;
import com.mobile.framework.core.Platform;
import com.mobile.tests.utils.TestNGListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;


@Listeners(TestNGListener.class)
@Test(groups = "android")
public class AndroidDeviceManagersTest {

    @BeforeMethod
    public void setupEnvironment() throws IOException, InterruptedException {
        Platform.setCurrent(Platform.ANDROID);

        if (DeviceManagers.current().isAppInstalled(MobileConfig.ANDROID_APP_PACKAGE)) {
            DeviceManagers.current().uninstallApp(MobileConfig.ANDROID_APP_PACKAGE);
        }
    }

    @Test
    public void installAppTestPositive() throws IOException, InterruptedException {
        DeviceManagers.current().installApp(MobileConfig.ANDROID_APK_PATH);
        Assert.assertTrue(DeviceManagers.current().isAppInstalled(MobileConfig.ANDROID_APP_PACKAGE));
    }

    @Test
    public void installAppTestNegative_AlreadyExist() throws IOException, InterruptedException {
        DeviceManagers.current().installApp(MobileConfig.ANDROID_APK_PATH);

        RuntimeException exception = Assert.expectThrows(
                RuntimeException.class,
                () -> DeviceManagers.current().installApp(MobileConfig.ANDROID_APK_PATH)
        );

        Assert.assertTrue(exception.getMessage().contains("INSTALL_FAILED_ALREADY_EXISTS"));
    }

    @Test
    public void startAppTestPositive() throws IOException, InterruptedException {
        DeviceManagers.current().installApp(MobileConfig.ANDROID_APK_PATH);
        Assert.assertFalse(DeviceManagers.current().isAppRunning(MobileConfig.ANDROID_APP_PACKAGE),
                "App is already running right after install");

        DeviceManagers.current().startApp(MobileConfig.ANDROID_APP_PACKAGE);

        Assert.assertTrue(DeviceManagers.current().isAppRunning(MobileConfig.ANDROID_APP_PACKAGE),
                "App is not running after startApp");
    }

    @Test
    public void reinstallAppTestPositive() throws IOException, InterruptedException {
        DeviceManagers.current().installApp(MobileConfig.ANDROID_APK_PATH);
        DeviceManagers.current().reinstallApp(MobileConfig.ANDROID_APK_PATH, MobileConfig.ANDROID_APP_PACKAGE);

        Assert.assertTrue(DeviceManagers.current().isAppInstalled(MobileConfig.ANDROID_APP_PACKAGE));
    }
}
