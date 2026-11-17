package com.esha.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class ProductsPage extends BasePage {

    private final By title = By.cssSelector("[data-test='title']");
    private final By cartLink = By.cssSelector("[data-test='shopping-cart-link']");
    private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By sortDropdown = By.cssSelector("[data-test='product-sort-container']");
    private final By inventoryItems = By.cssSelector("[data-test='inventory-item']");
    private final By productNames = By.cssSelector("[data-test='inventory-item-name']");
    private final By productPrices = By.cssSelector("[data-test='inventory-item-price']");
    private final By productDescriptions = By.cssSelector("[data-test='inventory-item-desc']");
    private final By productImages = By.cssSelector("img.inventory_item_img");
    private final By addToCartButtons = By.cssSelector("button[id^='add-to-cart']");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        waitForUrlContains("inventory.html");
        return getText(title);
    }

    // ---------- product listing ----------

    public int getInventoryItemCount() {
        waitVisible(productNames);
        return driver.findElements(inventoryItems).size();
    }

    public List<String> getProductNames() {
        waitVisible(productNames);
        return getTexts(productNames);
    }

    public List<String> getProductPriceTexts() {
        waitVisible(productPrices);
        return getTexts(productPrices);
    }

    public List<Double> getProductPrices() {
        return getProductPriceTexts().stream().map(BasePage::parseAmount).collect(Collectors.toList());
    }

    public List<String> getProductDescriptions() {
        waitVisible(productDescriptions);
        return getTexts(productDescriptions);
    }

    public List<WebElement> getAddToCartButtons() {
        waitVisible(addToCartButtons);
        return driver.findElements(addToCartButtons);
    }

    /** Number of product images that really loaded (not broken). Waits briefly for slow images. */
    public int countLoadedImages() {
        waitVisible(productNames);
        ExpectedCondition<Boolean> allImagesLoaded =
                d -> loadedImageCount() == driver.findElements(productImages).size();
        try {
            wait.until(allImagesLoaded);
        } catch (TimeoutException ignored) {
            // fall through: report whatever number actually loaded
        }
        return loadedImageCount();
    }

    private int loadedImageCount() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        int loaded = 0;
        for (WebElement img : driver.findElements(productImages)) {
            Object ok = js.executeScript("return arguments[0].complete && arguments[0].naturalWidth > 0;", img);
            if (Boolean.TRUE.equals(ok)) {
                loaded++;
            }
        }
        return loaded;
    }

    public void openProductDetails(String productName) {
        click(By.linkText(productName));
    }

    // ---------- cart ----------

    public void addToCart(String productName) {
        click(By.id("add-to-cart-" + toSlug(productName)));
    }

    public boolean isCartBadgeDisplayed() {
        return !driver.findElements(cartBadge).isEmpty();
    }

    /** Number shown on the cart badge; 0 when the badge is not shown. */
    public int getCartCount() {
        try {
            ExpectedCondition<WebElement> badgeShown =
                    d -> d.findElements(cartBadge).stream().findFirst().orElse(null);
            WebElement badge = new WebDriverWait(driver, Duration.ofSeconds(3)).until(badgeShown);
            return Integer.parseInt(badge.getText().trim());
        } catch (TimeoutException e) {
            return 0;
        }
    }

    public void openCart() {
        click(cartLink);
    }

    // ---------- sorting ----------

    public void sortBy(String visibleText) {
        new Select(waitVisible(sortDropdown)).selectByVisibleText(visibleText);
    }

    public List<String> getSortOptions() {
        return new Select(waitVisible(sortDropdown)).getOptions().stream()
                .map(WebElement::getText).collect(Collectors.toList());
    }

    // ---------- menu ----------

    public void logout() {
        click(menuButton);
        click(logoutLink);
    }
}
