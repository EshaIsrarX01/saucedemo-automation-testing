package com.esha.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPage extends BasePage {

    private final By title = By.cssSelector("[data-test='title']");
    private final By cartItems = By.cssSelector(".cart_item");
    private final By itemNames = By.cssSelector("[data-test='inventory-item-name']");
    private final By itemPrice = By.cssSelector("[data-test='inventory-item-price']");
    private final By itemQuantity = By.cssSelector("[data-test='item-quantity']");
    private final By itemDescription = By.cssSelector("[data-test='inventory-item-desc']");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        waitForUrlContains("cart.html");
        return getText(title);
    }

    public List<String> getItemNames() {
        getPageTitle(); // makes sure the cart page has loaded
        return getTexts(itemNames);
    }

    public int getItemCount() {
        getPageTitle();
        return driver.findElements(cartItems).size();
    }

    public String getFirstItemPrice() {
        getPageTitle();
        return getText(itemPrice);
    }

    public String getFirstItemQuantity() {
        getPageTitle();
        return getText(itemQuantity);
    }

    public String getFirstItemDescription() {
        getPageTitle();
        return getText(itemDescription);
    }

    public void removeItem(String productName) {
        click(By.id("remove-" + toSlug(productName)));
    }

    public void clickCheckout() {
        click(checkoutButton);
    }

    public void continueShopping() {
        click(continueShoppingButton);
    }

    /**
     * Clicks Checkout and reports whether the app let the user through to "Checkout: Your Information".
     * Waits up to 3 seconds before concluding the user was blocked, so a slow page cannot cause a false "blocked".
     */
    public boolean canProceedToCheckout() {
        clickCheckout();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.urlContains("checkout-step-one"));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
