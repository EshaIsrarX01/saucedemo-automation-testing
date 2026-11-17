package com.esha.saucedemo.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

/** Builds a configured Chrome driver. Selenium Manager downloads the matching ChromeDriver automatically. */
public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();

        // Stop Chrome's "save password" / "password leaked" popups from covering the page
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);

        options.addArguments("--incognito", "--disable-notifications", "--disable-infobars");

        boolean headless = ConfigReader.getBoolean("headless");
        if (headless) {
            options.addArguments("--headless=new", "--window-size=1920,1080",
                    "--no-sandbox", "--disable-dev-shm-usage");
        }

        WebDriver driver = new ChromeDriver(options);
        if (!headless) {
            driver.manage().window().maximize();
        }
        return driver;
    }
}
