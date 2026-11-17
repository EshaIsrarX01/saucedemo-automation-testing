package com.esha.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutInformationPage extends BasePage {

    private final By title = By.cssSelector("[data-test='title']");
    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By postalCode = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public CheckoutInformationPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        waitForUrlContains("checkout-step-one");
        return getText(title);
    }

    public void fillInformation(String first, String last, String zip) {
        type(firstName, first);
        type(lastName, last);
        type(postalCode, zip);
    }

    public void clickContinue() {
        click(continueButton);
    }

    public void clickCancel() {
        click(cancelButton);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }
}
