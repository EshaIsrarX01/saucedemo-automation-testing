package com.esha.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {

    private final By title = By.cssSelector("[data-test='title']");
    private final By completeHeader = By.cssSelector("[data-test='complete-header']");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        waitForUrlContains("checkout-complete");
        return getText(title);
    }

    public String getConfirmationHeader() {
        waitForUrlContains("checkout-complete");
        return getText(completeHeader);
    }
}
