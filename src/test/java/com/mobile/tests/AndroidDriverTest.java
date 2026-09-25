package com.mobile.tests;

import com.mobile.framework.config.MobileConfig;
import com.mobile.framework.core.DriverHolder;
import io.appium.java_client.android.AndroidDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "android")
public class AndroidDriverTest extends BaseTest {

    @Test
    public void sessionIDIsNotNullTest() {
        Assert.assertNotNull(DriverHolder.driver().getSessionId());
    }

    @Test
    public void packageIsCorrectTest() {
        AndroidDriver androidDriver = (AndroidDriver) DriverHolder.driver();
        Assert.assertEquals(androidDriver.getCurrentPackage(), MobileConfig.ANDROID_APP_PACKAGE);
    }

    @Test
    public void windowSizeIsAvailableTest() {
        Assert.assertTrue(DriverHolder.driver().manage().window().getSize().height > 0 &&
                DriverHolder.driver().manage().window().getSize().width > 0);
    }
}
