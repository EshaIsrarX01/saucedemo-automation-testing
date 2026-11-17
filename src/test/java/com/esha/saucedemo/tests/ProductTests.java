package com.esha.saucedemo.tests;

import com.esha.saucedemo.base.BaseTest;
import com.esha.saucedemo.pages.LoginPage;
import com.esha.saucedemo.pages.ProductDetailsPage;
import com.esha.saucedemo.pages.ProductsPage;
import com.esha.saucedemo.utils.TestData;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.esha.saucedemo.utils.ReportLogger.step;

/** Automates the Product_Test_Cases sheet (TC_PRODUCT_001 - TC_PRODUCT_010). */
public class ProductTests extends BaseTest {

    @Test(description = "TC_PRODUCT_001 - Verify Products page is displayed after successful login",
            groups = {"smoke", "regression", "product"})
    public void TC_PRODUCT_001_productsPageAfterLogin() {
        LoginPage login = new LoginPage(driver);
        step("1. Enter valid username");
        step("2. Enter valid password");
        step("3. Click Login");
        login.login(TestData.STANDARD_USER, TestData.PASSWORD);

        Assert.assertEquals(new ProductsPage(driver).getPageTitle(), "Products");
    }

    @Test(description = "TC_PRODUCT_002 - Verify product listings are displayed",
            groups = {"regression", "product"})
    public void TC_PRODUCT_002_productListingsDisplayed() {
        ProductsPage products = loginAsStandardUser();
        step("1. Observe the product listing section");
        step("2. Verify that product cards are displayed");

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(products.getInventoryItemCount(), TestData.PRODUCT_COUNT, "Number of product cards");
        soft.assertEquals(products.getProductNames().size(), TestData.PRODUCT_COUNT, "Number of product names");
        soft.assertEquals(products.getProductPriceTexts().size(), TestData.PRODUCT_COUNT, "Number of prices");
        soft.assertEquals(products.getProductDescriptions().size(), TestData.PRODUCT_COUNT, "Number of descriptions");
        soft.assertAll();
    }

    @Test(description = "TC_PRODUCT_003 - Verify product names are displayed correctly and completely",
            groups = {"regression", "product"})
    public void TC_PRODUCT_003_productNamesCorrect() {
        ProductsPage products = loginAsStandardUser();
        step("1. Review the names of all displayed products");
        List<String> actual = new ArrayList<>(products.getProductNames());
        step("2. Compare the displayed names with the expected product data");

        List<String> expected = new ArrayList<>(TestData.PRODUCT_NAMES);
        Collections.sort(actual);
        Collections.sort(expected);
        Assert.assertEquals(actual, expected,
                "Product names must be displayed completely, including Test.allTheThings() T-Shirt (Red)");
    }

    @Test(description = "TC_PRODUCT_004 - Verify product prices are displayed",
            groups = {"regression", "product"})
    public void TC_PRODUCT_004_productPricesDisplayed() {
        ProductsPage products = loginAsStandardUser();
        step("1. Observe each product card");
        step("2. Verify that a price is displayed");

        List<String> prices = products.getProductPriceTexts();
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(prices.size(), TestData.PRODUCT_COUNT, "Number of prices");
        for (String price : prices) {
            soft.assertTrue(price.matches("\\$\\d+\\.\\d{2}"), "Price has unexpected format: '" + price + "'");
        }
        soft.assertAll();
    }

    @Test(description = "TC_PRODUCT_005 - Verify product images are displayed",
            groups = {"regression", "product"})
    public void TC_PRODUCT_005_productImagesDisplayed() {
        ProductsPage products = loginAsStandardUser();
        step("1. Observe each product card");
        step("2. Verify that product images are loaded");

        Assert.assertEquals(products.countLoadedImages(), TestData.PRODUCT_COUNT,
                "Every product must show a loaded (not broken) image");
    }

    @Test(description = "TC_PRODUCT_006 - Verify product descriptions are displayed",
            groups = {"regression", "product"})
    public void TC_PRODUCT_006_productDescriptionsDisplayed() {
        ProductsPage products = loginAsStandardUser();
        step("1. Observe each product card");
        step("2. Verify that the product description is visible");

        List<String> descriptions = products.getProductDescriptions();
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(descriptions.size(), TestData.PRODUCT_COUNT, "Number of descriptions");
        for (String description : descriptions) {
            soft.assertFalse(description.trim().isEmpty(), "A product description is empty");
        }
        soft.assertAll();
    }

    @Test(description = "TC_PRODUCT_007 - Verify Add to Cart button is available for products",
            groups = {"regression", "product"})
    public void TC_PRODUCT_007_addToCartButtonsAvailable() {
        ProductsPage products = loginAsStandardUser();
        step("1. Observe each product card");
        step("2. Verify the Add to Cart button is displayed");

        List<WebElement> buttons = products.getAddToCartButtons();
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(buttons.size(), TestData.PRODUCT_COUNT, "Number of Add to cart buttons");
        for (WebElement button : buttons) {
            soft.assertTrue(button.isDisplayed() && button.isEnabled(), "Button not usable: " + button.getAttribute("id"));
            soft.assertEquals(button.getText(), "Add to cart", "Button label");
        }
        soft.assertAll();
    }

    @Test(description = "TC_PRODUCT_008 - Verify navigation to product details page",
            groups = {"regression", "product"})
    public void TC_PRODUCT_008_navigateToProductDetails() {
        ProductsPage products = loginAsStandardUser();
        step("1. Click on a product name: " + TestData.BACKPACK);
        products.openProductDetails(TestData.BACKPACK);

        ProductDetailsPage details = new ProductDetailsPage(driver);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(details.getProductName(), TestData.BACKPACK, "Product shown on the details page");
        soft.assertTrue(driver.getCurrentUrl().contains("inventory-item"), "URL should be the product details page");
        soft.assertAll();
    }

    @Test(description = "TC_PRODUCT_009 - Verify user can return from product details to Products page",
            groups = {"regression", "product"})
    public void TC_PRODUCT_009_returnFromProductDetails() {
        ProductsPage products = loginAsStandardUser();
        products.openProductDetails(TestData.BACKPACK);
        ProductDetailsPage details = new ProductDetailsPage(driver);
        Assert.assertEquals(details.getProductName(), TestData.BACKPACK, "Precondition: product details page is open");

        step("1. Click the Back to Products button");
        details.clickBackToProducts();

        Assert.assertEquals(products.getPageTitle(), "Products", "User should be returned to the Products page");
    }

    @Test(description = "TC_PRODUCT_010 - Verify Products page is accessible after logging in again",
            groups = {"regression", "product"})
    public void TC_PRODUCT_010_productsPageAfterRelogin() {
        ProductsPage products = loginAsStandardUser();
        products.logout();
        Assert.assertTrue(new LoginPage(driver).isLoginButtonDisplayed(), "Precondition: user has logged out");

        step("1. Log in using valid credentials");
        new LoginPage(driver).login(TestData.STANDARD_USER, TestData.PASSWORD);
        step("2. Observe the Products page");

        ProductsPage again = new ProductsPage(driver);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(again.getPageTitle(), "Products", "Page title");
        soft.assertEquals(again.getInventoryItemCount(), TestData.PRODUCT_COUNT, "Products loaded without errors");
        soft.assertAll();
    }
}
