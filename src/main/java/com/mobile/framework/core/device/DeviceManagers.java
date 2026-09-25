package com.mobile.framework.core.device;

import com.mobile.framework.core.Platform;
/**
 * Provides cached access to the device manager for the currently selected platform.
 */
public final class DeviceManagers {

    private static DeviceManager android;
    private static DeviceManager ios;

    private DeviceManagers() {
    }

    public static DeviceManager current() {
        if (Platform.current().isAndroid()) {
            if (android == null) {
                android = new AndroidDeviceManager();
            }
            return android;
        }

        if (ios == null) {
            ios = new IosDeviceManager();
        }
        return ios;
    }
}
