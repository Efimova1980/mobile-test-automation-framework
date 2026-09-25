package com.mobile.framework.config;

/**
 * Test run configuration.
 *
 * iOS values are empty: running on iOS requires macOS with Xcode and a booted simulator.
 * Download a simulator build of My Demo App
 * (https://github.com/saucelabs/my-demo-app-ios/releases) and fill in the IOS_* values.
 */
public class MobileConfig {

    public static final String ANDROID_APP_PACKAGE = "com.saucelabs.mydemoapp.android";

    public static final String ANDROID_APP_ACTIVITY =
            "com.saucelabs.mydemoapp.android.view.activities.SplashActivity";

    public static final String ANDROID_APK_PATH =
            "src/test/java/com/mobile/tests/resources/mda-2.2.0-25.apk";

    public static final String ANDROID_DEVICE_NAME = "emulator-5554";

    public static final String APPIUM_URL = "http://127.0.0.1:4723";


    //iOS only
    public static final String IOS_BUNDLE_ID = "";

    public static final String IOS_APP_PATH = "";

    public static final String IOS_DEVICE_NAME = "";

    private MobileConfig() {
    }
}
