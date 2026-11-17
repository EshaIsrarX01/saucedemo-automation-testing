package com.esha.saucedemo.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

/** Creates the single ExtentReports object that writes target/extent-report/ExtentReport.html. */
public final class ExtentManager {

    private static final String REPORT_DIR = "target/extent-report";
    private static ExtentReports extent;

    private ExtentManager() {
    }

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            new File(REPORT_DIR).mkdirs();

            ExtentSparkReporter spark = new ExtentSparkReporter(REPORT_DIR + "/ExtentReport.html");
            spark.config().setDocumentTitle("SauceDemo Automation Report");
            spark.config().setReportName("SauceDemo Selenium Regression Suite");
            spark.config().setTheme(Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Tester", "Esha Israr");
            extent.setSystemInfo("Application", ConfigReader.get("baseUrl"));
            extent.setSystemInfo("Browser", "Google Chrome");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));
        }
        return extent;
    }
}
