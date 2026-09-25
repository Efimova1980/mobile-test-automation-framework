package com.mobile.framework.core;

import com.mobile.framework.config.MobileConfig;

public enum Platform {
    ANDROID(MobileConfig.ANDROID_APP_PACKAGE, MobileConfig.ANDROID_APK_PATH),
    IOS(MobileConfig.IOS_BUNDLE_ID, MobileConfig.IOS_APP_PATH);

    private static Platform current;

    private final String appId;
    private final String appPath;

    Platform(String appId, String appPath) {
        this.appId = appId;
        this.appPath = appPath;
    }

    public static Platform current() {
        if (current == null) {
            current = configured();
        }
        return current;
    }

    /**
     * Platform selected for the test run: "platform" system property,
     * then PLATFORM environment variable, ANDROID by default.
     */
    public static Platform configured() {
        String env = System.getProperty("platform", System.getenv().getOrDefault("PLATFORM", "ANDROID"));
        return Platform.valueOf(env.toUpperCase());
    }

    public static void setCurrent(Platform platform) {
        current = platform;
    }

    /** Android package or iOS bundle id of the application under test. */
    public String appId()   { return appId; }

    /** Path to the .apk / .app file of the application under test. */
    public String appPath() { return appPath; }

    public boolean isAndroid() { return this == ANDROID; }
    public boolean isIos()     { return this == IOS; }
}
