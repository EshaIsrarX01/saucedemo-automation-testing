package com.esha.saucedemo.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtil {

    private static final Path SCREENSHOT_DIR = Paths.get("target", "screenshots");

    private ScreenshotUtil() {
    }

    /** Base64 string, embedded straight into the HTML report so the report is one self-contained file. */
    public static String asBase64(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    /** Saves a PNG under target/screenshots and returns its path. */
    public static Path saveToFile(WebDriver driver, String testName) {
        try {
            Files.createDirectories(SCREENSHOT_DIR);
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String safeName = testName.replaceAll("[^a-zA-Z0-9_-]", "_");
            Path file = SCREENSHOT_DIR.resolve(safeName + "_" + time + ".png");
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(file, bytes);
            return file;
        } catch (IOException e) {
            throw new IllegalStateException("Could not save screenshot", e);
        }
    }
}
