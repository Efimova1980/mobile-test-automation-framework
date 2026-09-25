package com.mobile.tests;

import com.mobile.framework.config.MobileConfig;
import com.mobile.framework.core.Platform;
import com.mobile.framework.core.device.DeviceManagers;
import com.mobile.tests.utils.TestNGListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;

@Listeners(TestNGListener.class)
@Test(groups = "ios")
public class IosDeviceManagersTest {

    @BeforeMethod
    public void setupEnvironment() throws IOException, InterruptedException {
        Platform.setCurrent(Platform.IOS);

        if (DeviceManagers.current().isAppInstalled(MobileConfig.IOS_BUNDLE_ID)) {
            DeviceManagers.current().uninstallApp(MobileConfig.IOS_BUNDLE_ID);
        }
    }

    @Test
    public void installAppTestPositive() throws IOException, InterruptedException {
        DeviceManagers.current().installApp(MobileConfig.IOS_APP_PATH);
        Assert.assertTrue(DeviceManagers.current().isAppInstalled(MobileConfig.IOS_BUNDLE_ID));
    }

    @Test
    public void uninstallAppTestPositive() throws IOException, InterruptedException {
        DeviceManagers.current().installApp(MobileConfig.IOS_APP_PATH);
        DeviceManagers.current().uninstallApp(MobileConfig.IOS_BUNDLE_ID);
        Assert.assertFalse(DeviceManagers.current().isAppInstalled(MobileConfig.IOS_BUNDLE_ID));
    }
}
