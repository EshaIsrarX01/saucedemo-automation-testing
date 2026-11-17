package com.esha.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailsPage extends BasePage {

    private final By productName = By.cssSelector("[data-test='inventory-item-name']");
    private final By backButton = By.id("back-to-products");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String getProductName() {
        waitForUrlContains("inventory-item.html");
        return getText(productName);
    }

    public void clickBackToProducts() {
        click(backButton);
    }
}
