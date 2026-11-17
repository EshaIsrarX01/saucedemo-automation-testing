package com.esha.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.esha.saucedemo.utils.ConfigReader;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/** Shared helpers for every page object. All waiting is explicit: no Thread.sleep anywhere. */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicitWait")));
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String text) {
        WebElement element = waitVisible(locator);
        element.clear();
        if (text != null && !text.isEmpty()) {
            element.sendKeys(text);
        }
    }

    protected String getText(By locator) {
        return waitVisible(locator).getText();
    }

    /** Text of every element matching the locator (empty list if none; does not wait). */
    protected List<String> getTexts(By locator) {
        return driver.findElements(locator).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitVisible(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Waiting for the URL avoids reading the previous page's title during navigation. */
    protected void waitForUrlContains(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }

    /** "Sauce Labs Backpack" -> "sauce-labs-backpack" (how SauceDemo builds its button ids). */
    protected static String toSlug(String productName) {
        return productName.toLowerCase().replace(" ", "-");
    }

    /** "Item total: $29.99" -> 29.99 */
    protected static double parseAmount(String text) {
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }
}
