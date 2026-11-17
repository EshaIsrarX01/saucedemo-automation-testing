package com.esha.saucedemo.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.esha.saucedemo.base.BaseTest;
import com.esha.saucedemo.utils.ExtentManager;
import com.esha.saucedemo.utils.ReportLogger;
import com.esha.saucedemo.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Hooks into TestNG: writes every test result to the Extent report and
 * attaches a screenshot when a test fails.
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentManager.getInstance().createTest(buildName(result));
        String[] groups = result.getMethod().getGroups();
        if (groups.length > 0) {
            test.assignCategory(groups);
        }
        ReportLogger.setTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ReportLogger.getTest().pass("Test passed: actual result matched the expected result");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ReportLogger.getTest();
        test.fail(result.getThrowable());

        Object instance = result.getInstance();
        if (instance instanceof BaseTest) {
            WebDriver driver = ((BaseTest) instance).getDriver();
            if (driver != null) {
                String base64 = ScreenshotUtil.asBase64(driver);
                test.fail("Screenshot at the point of failure",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
                ScreenshotUtil.saveToFile(driver, result.getMethod().getMethodName());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ReportLogger.getTest();
        if (test == null) {
            test = ExtentManager.getInstance().createTest(buildName(result));
        }
        if (result.getThrowable() != null) {
            test.skip(result.getThrowable());
        } else {
            test.skip("Test skipped");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.getInstance().flush();
    }

    /** Name shown in the report: the @Test description (which starts with the TC id). */
    private String buildName(ITestResult result) {
        String name = result.getMethod().getDescription();
        if (name == null || name.isEmpty()) {
            name = result.getMethod().getMethodName();
        }
        return name;
    }
}
