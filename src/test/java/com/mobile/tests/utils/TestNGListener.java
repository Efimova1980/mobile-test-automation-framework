package com.mobile.tests.utils;
import com.mobile.framework.core.DriverHolder;
import com.mobile.framework.core.device.DeviceManagers;
import org.openqa.selenium.OutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Files;
import java.nio.file.Path;
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
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());
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

        if (DriverHolder.hasDriver()) {
            try {
                Path screenshotsDir = Path.of("build", "screenshots");
                Files.createDirectories(screenshotsDir);
                Path screenshot = screenshotsDir.resolve("device_scrn" + System.currentTimeMillis() + ".png");

                Files.write(screenshot, DriverHolder.driver().getScreenshotAs(OutputType.BYTES));
                logger.info("Screenshot saved to {}", screenshot);
            } catch (Exception e) {
                logger.warn("Failed to take screenshot", e);
            }
        }

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ITestListener.super.onTestSkipped(result);
        logger.info("Test '{}.{}' skipped",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        ITestListener.super.onTestStart(result);
        logger.info("Test '{}.{}' started",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ITestListener.super.onTestSuccess(result);
        logger.info("Test '{}.{}' passed successfully",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());
    }
}
