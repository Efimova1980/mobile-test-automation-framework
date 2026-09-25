package com.mobile.framework.core.device;

import java.io.IOException;
import java.util.List;

/**
 * Executes Android device operations through ADB.
 */
public final class AndroidDeviceManager implements DeviceManager {

    @Override
    public void installApp(String appPath) throws IOException, InterruptedException {
        DeviceCommandRunner.runCommand(List.of("adb", "install", appPath));
    }

    @Override
    public void reinstallApp(String appPath, String appId) throws IOException, InterruptedException {
        DeviceCommandRunner.runCommand(List.of("adb", "install", "-r", appPath));
    }

    @Override
    public void uninstallApp(String appId) throws IOException, InterruptedException {
        DeviceCommandRunner.runCommand(List.of("adb", "uninstall", appId));
    }

    @Override
    public void startApp(String appId, String activityName) throws IOException, InterruptedException {
        DeviceCommandRunner.runCommand(List.of("adb", "shell", "am", "start", "-n",
                appId + "/" + activityName));
    }

    @Override
    public void stopApp(String appId) throws IOException, InterruptedException {
        DeviceCommandRunner.runCommand(List.of("adb", "shell", "am", "force-stop", appId));
    }

    @Override
    public void clearLogs() throws IOException, InterruptedException {
        DeviceCommandRunner.runCommand(List.of("adb", "logcat", "-c"));
    }

    @Override
    public String getLogs() throws IOException, InterruptedException {
        return DeviceCommandRunner.runCommand(List.of("adb", "logcat", "-d", "-t", "100", "*:E"));
    }

    @Override
    public boolean isAppInstalled(String appId) throws IOException, InterruptedException {
        String output = DeviceCommandRunner.runCommand(List.of("adb", "shell", "pm", "list", "packages", appId));
        return output.contains(appId);
    }
}
