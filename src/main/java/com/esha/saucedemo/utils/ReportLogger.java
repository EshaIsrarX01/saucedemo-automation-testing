package com.esha.saucedemo.utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.WebDriver;

/**
 * Lets tests write their manual-test steps (and screenshots) into the Extent report.
 * The listener registers the current test; tests just call ReportLogger.step("...").
 */
public final class ReportLogger {

    private static final ThreadLocal<ExtentTest> CURRENT = new ThreadLocal<>();

    private ReportLogger() {
    }

    public static void setTest(ExtentTest test) {
        CURRENT.set(test);
    }

    public static ExtentTest getTest() {
        return CURRENT.get();
    }

    public static void step(String message) {
        ExtentTest test = CURRENT.get();
        if (test != null) {
            test.info(message);
        }
    }

    public static void stepWithScreenshot(WebDriver driver, String message) {
        ExtentTest test = CURRENT.get();
        if (test != null) {
            test.info(message,
                    MediaEntityBuilder.createScreenCaptureFromBase64String(ScreenshotUtil.asBase64(driver)).build());
        }
    }
}
