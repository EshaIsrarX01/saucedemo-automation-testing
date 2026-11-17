package com.esha.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class CheckoutOverviewPage extends BasePage {

    private final By title = By.cssSelector("[data-test='title']");
    private final By itemNames = By.cssSelector("[data-test='inventory-item-name']");
    private final By itemQuantity = By.cssSelector("[data-test='item-quantity']");
    private final By itemPrice = By.cssSelector("[data-test='inventory-item-price']");
    private final By subtotalLabel = By.cssSelector("[data-test='subtotal-label']");
    private final By taxLabel = By.cssSelector("[data-test='tax-label']");
    private final By totalLabel = By.cssSelector("[data-test='total-label']");
    private final By finishButton = By.id("finish");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        waitForUrlContains("checkout-step-two");
        return getText(title);
    }

    public List<String> getItemNames() {
        getPageTitle();
        return getTexts(itemNames);
    }

    public String getFirstItemQuantity() {
        getPageTitle();
        return getText(itemQuantity);
    }

    public String getFirstItemPrice() {
        getPageTitle();
        return getText(itemPrice);
    }

    public double getSubtotal() {
        return parseAmount(getText(subtotalLabel));
    }

    public double getTax() {
        return parseAmount(getText(taxLabel));
    }

    public double getTotal() {
        return parseAmount(getText(totalLabel));
    }

    public void clickFinish() {
        click(finishButton);
    }
}
