package com.mobile.framework.core.device;

import java.io.IOException;

/**
 * Defines common device operations for platform-specific mobile device managers.
 */
public interface DeviceManager {

    void installApp(String appPath) throws IOException, InterruptedException;

    void reinstallApp(String appPath, String appId) throws IOException, InterruptedException;

    void uninstallApp(String appId) throws IOException, InterruptedException;

    void startApp(String appId, String activityName) throws IOException, InterruptedException;

    void stopApp(String appId) throws IOException, InterruptedException;

    void clearLogs() throws IOException, InterruptedException;

    String getLogs() throws IOException, InterruptedException;

    boolean isAppInstalled(String appId) throws IOException, InterruptedException;
}
