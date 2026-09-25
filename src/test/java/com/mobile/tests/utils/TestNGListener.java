package com.mobile.tests.utils;
import com.mobile.framework.core.device.DeviceManagers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.LocalDateTime;


public class TestNGListener implements ITestListener{
    Logger logger = LoggerFactory.getLogger(TestNGListener.class);

    @Override
    public void onFinish(ITestContext context) {
        ITestListener.super.onFinish(context);
        logger.info("Test execution finished at {}", LocalDateTime.now());
        logger.info("Passed: {}", context.getPassedTests().size());
        logger.info("Failed: {}", context.getFailedTests().size());
        logger.info("Skipped: {}", context.getSkippedTests().size());
    }

    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
        logger.info("Execution started at {}", LocalDateTime.now());
        logger.info("Suite: {}", context.getSuite().getName());
        logger.info("Test: {}", context.getName());
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
        logger.info("Test '{}.{}' failed with timeout",
                result.getMethod().getMethodName(),
                result.getTestClass().getRealClass().getSimpleName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ITestListener.super.onTestFailure(result);

        String testName = result.getMethod().getMethodName();
        logger.error("Test '{}.{}' failed",
                result.getTestClass().getRealClass().getSimpleName(),
                testName);

        logger.error("Exception:", result.getThrowable());

        try {
            logger.error("========== DEVICE LOGS ==========");
            logger.error(DeviceManagers.current().getLogs());
            logger.error("============================");
        } catch (Exception e) {
            logger.warn("Failed to get device logs", e);
        }

        try {
            String fName =  "device_scrn" + System.currentTimeMillis();
            DeviceManagers.current().takeScreenshot(fName);
            logger.info("Screenshot saved to build/screenshots/{}.png", fName);
        } catch (Exception e) {
            logger.warn("Failed to take screenshot", e);
        }

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ITestListener.super.onTestSkipped(result);
        logger.info("Test '{}.{}' skipped",
                result.getMethod().getMethodName(),
                result.getTestClass().getRealClass().getSimpleName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        ITestListener.super.onTestStart(result);
        logger.info("Test '{}.{}' started",
                result.getMethod().getMethodName(),
                result.getTestClass().getRealClass().getSimpleName());

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ITestListener.super.onTestSuccess(result);
        logger.info("Test '{}.{}' passed successfully",
                result.getMethod().getMethodName(),
                result.getTestClass().getRealClass().getSimpleName());
    }
}
