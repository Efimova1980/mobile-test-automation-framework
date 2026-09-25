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


}
