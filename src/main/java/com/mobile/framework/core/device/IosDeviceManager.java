package com.mobile.framework.core.device;

import java.io.IOException;
import java.util.List;

/**
 * Executes iOS simulator operations through xcrun simctl.
 */
public final class IosDeviceManager implements DeviceManager {

    private static final String IOS_DEVICE = "booted";

    @Override
    public void installApp(String appPath) throws IOException, InterruptedException {
        DeviceCommandRunner.runCommand(List.of("xcrun", "simctl", "install", IOS_DEVICE, appPath));
    }

    @Override
    public void reinstallApp(String appPath, String appId) throws IOException, InterruptedException {
        uninstallAppIfInstalled(appId);
        installApp(appPath);
    }

    @Override
    public void uninstallApp(String appId) throws IOException, InterruptedException {
        DeviceCommandRunner.runCommand(List.of("xcrun", "simctl", "uninstall", IOS_DEVICE, appId));
    }

    @Override
    public void startApp(String appId, String activityName) throws IOException, InterruptedException {
        DeviceCommandRunner.runCommand(List.of("xcrun", "simctl", "launch", IOS_DEVICE, appId));
    }

    @Override
    public void stopApp(String appId) throws IOException, InterruptedException {
        DeviceCommandRunner.runCommand(List.of("xcrun", "simctl", "terminate", IOS_DEVICE, appId));
    }

    @Override
    public void clearLogs() throws IOException, InterruptedException {
        DeviceCommandRunner.runCommand(List.of("xcrun", "simctl", "spawn", IOS_DEVICE, "log", "erase", "--all"));
    }

    @Override
    public String getLogs() throws IOException, InterruptedException {
        return DeviceCommandRunner.runCommand(List.of(
                "xcrun", "simctl", "spawn", IOS_DEVICE,
                "log", "show",
                "--style", "compact",
                "--last", "1m",
                "--predicate", "eventType == logEvent"
        ));
    }

    @Override
    public boolean isAppInstalled(String appId) throws IOException, InterruptedException {
        String output = DeviceCommandRunner.runCommand(List.of("xcrun", "simctl", "listapps", IOS_DEVICE));
        return output.contains("\"" + appId + "\"");
    }

    private void uninstallAppIfInstalled(String appId) throws IOException, InterruptedException {
        if (isAppInstalled(appId)) {
            uninstallApp(appId);
        }
    }
}
