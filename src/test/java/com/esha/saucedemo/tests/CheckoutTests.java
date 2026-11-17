package com.esha.saucedemo.tests;

import com.esha.saucedemo.base.BaseTest;
import com.esha.saucedemo.pages.CartPage;
import com.esha.saucedemo.pages.CheckoutCompletePage;
import com.esha.saucedemo.pages.CheckoutInformationPage;
import com.esha.saucedemo.pages.CheckoutOverviewPage;
import com.esha.saucedemo.pages.ProductsPage;
import com.esha.saucedemo.utils.ReportLogger;
import com.esha.saucedemo.utils.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Collections;

import static com.esha.saucedemo.utils.ReportLogger.step;

/** Automates the Checkout_Test_Cases sheet (TC_CHECKOUT_001 - TC_CHECKOUT_010). */
public class CheckoutTests extends BaseTest {

    /** Precondition: logged in, Sauce Labs Backpack in the cart, on "Checkout: Your Information". */
    private CheckoutInformationPage goToInformationPageWithBackpack() {
        ProductsPage products = loginAsStandardUser();
        products.addToCart(TestData.BACKPACK);
        products.openCart();
        new CartPage(driver).clickCheckout();
        CheckoutInformationPage info = new CheckoutInformationPage(driver);
        Assert.assertEquals(info.getPageTitle(), "Checkout: Your Information", "Precondition: information page open");
        return info;
    }

    /** Precondition: as above, plus valid customer information submitted, now on "Checkout: Overview". */
    private CheckoutOverviewPage goToOverviewWithBackpack() {
        CheckoutInformationPage info = goToInformationPageWithBackpack();
        info.fillInformation(TestData.FIRST_NAME, TestData.LAST_NAME, TestData.ZIP_CODE);
        info.clickContinue();
        CheckoutOverviewPage overview = new CheckoutOverviewPage(driver);
        Assert.assertEquals(overview.getPageTitle(), "Checkout: Overview", "Precondition: overview page open");
        return overview;
    }

    /** Expected result of the negative validation cases: error shown and user did not proceed. */
    private void assertValidationShown(CheckoutInformationPage info, String expectedError) {
        String actual = info.getErrorMessage();
        ReportLogger.stepWithScreenshot(driver, "Observed validation message: " + actual);
        SoftAssert soft = new SoftAssert();
        soft.assertTrue(actual.contains(expectedError), "Expected '" + expectedError + "' but was '" + actual + "'");
        soft.assertTrue(driver.getCurrentUrl().contains("checkout-step-one"), "User must not proceed past the information page");
        soft.assertAll();
    }

    @Test(description = "TC_CHECKOUT_001 - Verify user can access checkout with products in cart",
            groups = {"smoke", "regression", "checkout"})
    public void TC_CHECKOUT_001_accessCheckoutWithProducts() {
        ProductsPage products = loginAsStandardUser();
        products.addToCart(TestData.BACKPACK);
        step("1. Open the shopping cart");
        products.openCart();
        step("2. Click Checkout");
        new CartPage(driver).clickCheckout();

        Assert.assertEquals(new CheckoutInformationPage(driver).getPageTitle(), "Checkout: Your Information");
    }

    @Test(description = "TC_CHECKOUT_002 - Verify checkout continues with valid customer information",
            groups = {"regression", "checkout"})
    public void TC_CHECKOUT_002_validCustomerInformation() {
        CheckoutInformationPage info = goToInformationPageWithBackpack();
        step("1. Enter a valid first name: " + TestData.FIRST_NAME);
        step("2. Enter a valid last name: " + TestData.LAST_NAME);
        step("3. Enter a valid postal code: " + TestData.ZIP_CODE);
        info.fillInformation(TestData.FIRST_NAME, TestData.LAST_NAME, TestData.ZIP_CODE);
        step("4. Click Continue");
        info.clickContinue();

        Assert.assertEquals(new CheckoutOverviewPage(driver).getPageTitle(), "Checkout: Overview");
    }

    @Test(description = "TC_CHECKOUT_003 - Verify validation when first name is empty",
            groups = {"regression", "checkout"})
    public void TC_CHECKOUT_003_firstNameEmpty() {
        CheckoutInformationPage info = goToInformationPageWithBackpack();
        step("1. Leave the first name field empty");
        step("2. Enter a valid last name");
        step("3. Enter a valid postal code");
        info.fillInformation("", TestData.LAST_NAME, TestData.ZIP_CODE);
        step("4. Click Continue");
        info.clickContinue();

        assertValidationShown(info, "First Name is required");
    }

    @Test(description = "TC_CHECKOUT_004 - Verify validation when last name is empty",
            groups = {"regression", "checkout"})
    public void TC_CHECKOUT_004_lastNameEmpty() {
        CheckoutInformationPage info = goToInformationPageWithBackpack();
        step("1. Enter a valid first name");
        step("2. Leave the last name field empty");
        step("3. Enter a valid postal code");
        info.fillInformation(TestData.FIRST_NAME, "", TestData.ZIP_CODE);
        step("4. Click Continue");
        info.clickContinue();

        assertValidationShown(info, "Last Name is required");
    }

    @Test(description = "TC_CHECKOUT_005 - Verify validation when postal code is empty",
            groups = {"regression", "checkout"})
    public void TC_CHECKOUT_005_postalCodeEmpty() {
        CheckoutInformationPage info = goToInformationPageWithBackpack();
        step("1. Enter a valid first name");
        step("2. Enter a valid last name");
        step("3. Leave the postal code field empty");
        info.fillInformation(TestData.FIRST_NAME, TestData.LAST_NAME, "");
        step("4. Click Continue");
        info.clickContinue();

        assertValidationShown(info, "Postal Code is required");
    }

    @Test(description = "TC_CHECKOUT_006 - Verify order summary displays correct product and pricing information",
            groups = {"regression", "checkout"})
    public void TC_CHECKOUT_006_orderSummaryCorrect() {
        CheckoutOverviewPage overview = goToOverviewWithBackpack();
        step("1. Review the Checkout: Overview page");
        step("2. Verify the selected product");
        step("3. Verify item price and order totals");

        double subtotal = overview.getSubtotal();
        double tax = overview.getTax();
        double expectedTax = Math.round(subtotal * TestData.TAX_RATE * 100.0) / 100.0;

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(overview.getItemNames(), Collections.singletonList(TestData.BACKPACK), "Product");
        soft.assertEquals(overview.getFirstItemQuantity(), "1", "Quantity");
        soft.assertEquals(overview.getFirstItemPrice(), TestData.BACKPACK_PRICE_TEXT, "Item price");
        soft.assertEquals(subtotal, TestData.BACKPACK_PRICE, 0.001, "Subtotal (item total)");
        soft.assertEquals(tax, expectedTax, 0.01, "Tax (8%)");
        soft.assertEquals(overview.getTotal(), subtotal + tax, 0.01, "Total");
        soft.assertAll();
    }

    @Test(description = "TC_CHECKOUT_007 - Verify order can be completed using the Finish button",
            groups = {"smoke", "regression", "checkout"})
    public void TC_CHECKOUT_007_completeOrder() {
        CheckoutOverviewPage overview = goToOverviewWithBackpack();
        step("1. Review the order details");
        step("2. Click Finish");
        overview.clickFinish();

        Assert.assertEquals(new CheckoutCompletePage(driver).getPageTitle(), "Checkout: Complete!",
                "The order confirmation page should be displayed");
    }

    @Test(description = "TC_CHECKOUT_008 - Verify order confirmation is displayed after successful checkout",
            groups = {"regression", "checkout"})
    public void TC_CHECKOUT_008_orderConfirmationDisplayed() {
        CheckoutOverviewPage overview = goToOverviewWithBackpack();
        overview.clickFinish();
        step("1. Observe the confirmation page");

        Assert.assertEquals(new CheckoutCompletePage(driver).getConfirmationHeader(), TestData.CONFIRMATION_MESSAGE);
    }

    @Test(description = "TC_CHECKOUT_009 - Verify Cancel button returns user to the Cart page",
            groups = {"regression", "checkout"})
    public void TC_CHECKOUT_009_cancelReturnsToCart() {
        CheckoutInformationPage info = goToInformationPageWithBackpack();
        step("1. Click Cancel");
        info.clickCancel();

        Assert.assertEquals(new CartPage(driver).getPageTitle(), "Your Cart",
                "User should be returned to the Cart page without completing the checkout");
    }

    /**
     * KNOWN DEFECT - BUG_001.
     *
     * Follows the manual steps exactly: empty cart -> Checkout -> enter customer information -> Continue -> Finish.
     * The EXPECTED result is that the user is stopped and no order is placed. The application does not stop the
     * user, so this test FAILS, and the failure message lists every point at which the app should have blocked.
     * It uses soft assertions so the whole flow is exercised and documented with screenshots in the report.
     *
     * Tagged "known-defect" so testng-stable.xml (used by CI) excludes it. When the bug is fixed, this test passes.
     */
    @Test(description = "TC_CHECKOUT_010 - Verify checkout cannot be initiated with an empty cart (BUG_001)",
            groups = {"regression", "checkout", "known-defect"})
    public void TC_CHECKOUT_010_checkoutBlockedWithEmptyCart() {
        SoftAssert soft = new SoftAssert();

        ProductsPage products = loginAsStandardUser();
        step("1. Ensure the cart is empty");
        Assert.assertFalse(products.isCartBadgeDisplayed(), "Precondition failed: the cart is not empty");

        step("2. Open the shopping cart");
        products.openCart();
        CartPage cart = new CartPage(driver);
        Assert.assertEquals(cart.getItemCount(), 0, "Precondition failed: the cart page lists items");

        step("3. Click Checkout");
        boolean checkoutAllowed = cart.canProceedToCheckout();
        soft.assertFalse(checkoutAllowed,
                "Step 3: Checkout must be blocked when the cart is empty, but 'Checkout: Your Information' opened");

        if (checkoutAllowed) {
            CheckoutInformationPage info = new CheckoutInformationPage(driver);
            ReportLogger.stepWithScreenshot(driver, "Observed: 'Checkout: Your Information' opened with an empty cart");

            step("4. Enter valid customer information and click Continue");
            info.fillInformation(TestData.FIRST_NAME, TestData.LAST_NAME, TestData.ZIP_CODE);
            info.clickContinue();

            CheckoutOverviewPage overview = new CheckoutOverviewPage(driver);
            String overviewTitle = overview.getPageTitle();
            ReportLogger.stepWithScreenshot(driver, "Observed: " + overviewTitle + " with Item total $"
                    + overview.getSubtotal() + " and Total $" + overview.getTotal());
            soft.assertNotEquals(overviewTitle, "Checkout: Overview",
                    "Step 4: The Overview page must not be reachable with an empty cart (it showed a $0.00 total)");

            step("5. Click Finish on the Overview page");
            overview.clickFinish();

            String confirmation = new CheckoutCompletePage(driver).getConfirmationHeader();
            ReportLogger.stepWithScreenshot(driver, "Observed: confirmation page says '" + confirmation + "'");
            soft.assertNotEquals(confirmation, TestData.CONFIRMATION_MESSAGE,
                    "Step 5: No order must be placed with an empty cart, but '" + confirmation + "' was displayed");
        }

        soft.assertAll();
    }
}
