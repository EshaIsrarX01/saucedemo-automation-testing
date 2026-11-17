package com.esha.saucedemo.tests;

import com.esha.saucedemo.base.BaseTest;
import com.esha.saucedemo.pages.CartPage;
import com.esha.saucedemo.pages.ProductsPage;
import com.esha.saucedemo.utils.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Arrays;
import java.util.Collections;

import static com.esha.saucedemo.utils.ReportLogger.step;

/** Automates the Cart_Test_Cases sheet (TC_CART_001 - TC_CART_009). */
public class CartTests extends BaseTest {

    @Test(description = "TC_CART_001 - Verify a product can be added to the shopping cart",
            groups = {"smoke", "regression", "cart"})
    public void TC_CART_001_addProductToCart() {
        ProductsPage products = loginAsStandardUser();
        step("1. Select a product: " + TestData.BACKPACK);
        step("2. Click Add to Cart");
        products.addToCart(TestData.BACKPACK);
        step("3. Open the cart");
        products.openCart();

        Assert.assertEquals(new CartPage(driver).getItemNames(), Collections.singletonList(TestData.BACKPACK),
                "The selected product should be displayed in the cart");
    }

    @Test(description = "TC_CART_002 - Verify cart badge count is updated after adding a product",
            groups = {"regression", "cart"})
    public void TC_CART_002_cartBadgeUpdated() {
        ProductsPage products = loginAsStandardUser();
        Assert.assertFalse(products.isCartBadgeDisplayed(), "Precondition: the cart should be empty");
        step("1. Add one product to the cart");
        products.addToCart(TestData.BACKPACK);
        step("2. Observe the cart icon");

        Assert.assertEquals(products.getCartCount(), 1, "Cart badge should display 1");
    }

    @Test(description = "TC_CART_003 - Verify multiple products can be added to the cart",
            groups = {"regression", "cart"})
    public void TC_CART_003_addMultipleProducts() {
        ProductsPage products = loginAsStandardUser();
        step("1. Add the first product to the cart: " + TestData.BACKPACK);
        products.addToCart(TestData.BACKPACK);
        step("2. Add the second product to the cart: " + TestData.BIKE_LIGHT);
        products.addToCart(TestData.BIKE_LIGHT);
        step("3. Open the cart");
        products.openCart();
        step("4. Verify both products");

        Assert.assertEquals(new CartPage(driver).getItemNames(),
                Arrays.asList(TestData.BACKPACK, TestData.BIKE_LIGHT), "Both products should be in the cart");
    }

    @Test(description = "TC_CART_004 - Verify shopping cart page can be opened",
            groups = {"regression", "cart"})
    public void TC_CART_004_openCartPage() {
        ProductsPage products = loginAsStandardUser();
        products.addToCart(TestData.BACKPACK);
        step("1. Click the cart icon");
        products.openCart();

        CartPage cart = new CartPage(driver);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(cart.getPageTitle(), "Your Cart", "Cart page title");
        soft.assertEquals(cart.getItemNames(), Collections.singletonList(TestData.BACKPACK), "Selected product shown");
        soft.assertAll();
    }

    @Test(description = "TC_CART_005 - Verify product information is displayed correctly in the cart",
            groups = {"regression", "cart"})
    public void TC_CART_005_productInformationInCart() {
        ProductsPage products = loginAsStandardUser();
        products.addToCart(TestData.BACKPACK);
        step("1. Open the cart");
        products.openCart();
        step("2. Observe the product information");

        CartPage cart = new CartPage(driver);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(cart.getItemNames(), Collections.singletonList(TestData.BACKPACK), "Product name");
        soft.assertEquals(cart.getFirstItemPrice(), TestData.BACKPACK_PRICE_TEXT, "Product price");
        soft.assertEquals(cart.getFirstItemQuantity(), "1", "Product quantity");
        soft.assertFalse(cart.getFirstItemDescription().trim().isEmpty(), "Product description should be displayed");
        soft.assertAll();
    }

    @Test(description = "TC_CART_006 - Verify a product can be removed from the cart",
            groups = {"regression", "cart"})
    public void TC_CART_006_removeProductFromCart() {
        ProductsPage products = loginAsStandardUser();
        products.addToCart(TestData.BACKPACK);
        step("1. Open the cart");
        products.openCart();
        CartPage cart = new CartPage(driver);
        step("2. Click Remove for the selected product");
        cart.removeItem(TestData.BACKPACK);
        step("3. Observe the cart");

        Assert.assertEquals(cart.getItemCount(), 0, "The product should no longer be displayed in the cart");
    }

    @Test(description = "TC_CART_007 - Verify empty cart is displayed when no products are added",
            groups = {"regression", "cart"})
    public void TC_CART_007_emptyCart() {
        ProductsPage products = loginAsStandardUser();
        Assert.assertFalse(products.isCartBadgeDisplayed(), "Precondition: the cart should contain no products");
        step("1. Open the shopping cart");
        products.openCart();

        CartPage cart = new CartPage(driver);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(cart.getPageTitle(), "Your Cart", "Cart page opened");
        soft.assertEquals(cart.getItemCount(), 0, "No product items should be displayed");
        soft.assertAll();
    }

    @Test(description = "TC_CART_008 - Verify Continue Shopping button navigation",
            groups = {"regression", "cart"})
    public void TC_CART_008_continueShopping() {
        ProductsPage products = loginAsStandardUser();
        products.openCart();
        CartPage cart = new CartPage(driver);
        Assert.assertEquals(cart.getPageTitle(), "Your Cart", "Precondition: user is on the cart page");
        step("1. Click Continue Shopping");
        cart.continueShopping();

        Assert.assertEquals(products.getPageTitle(), "Products", "User should be redirected to the Products page");
    }

    @Test(description = "TC_CART_009 - Verify cart contents are retained when navigating between pages",
            groups = {"regression", "cart"})
    public void TC_CART_009_cartContentsRetained() {
        ProductsPage products = loginAsStandardUser();
        step("1. Add a product to the cart");
        products.addToCart(TestData.BACKPACK);
        step("2. Navigate to the cart");
        products.openCart();
        CartPage cart = new CartPage(driver);
        step("3. Click Continue Shopping");
        cart.continueShopping();
        Assert.assertEquals(products.getPageTitle(), "Products");
        step("4. Navigate back to the cart");
        products.openCart();
        step("5. Verify the previously added product");

        Assert.assertEquals(new CartPage(driver).getItemNames(), Collections.singletonList(TestData.BACKPACK),
                "The product should remain in the cart");
    }
}
