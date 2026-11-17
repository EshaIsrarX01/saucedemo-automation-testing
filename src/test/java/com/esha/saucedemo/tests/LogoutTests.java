package com.esha.saucedemo.tests;

import com.esha.saucedemo.base.BaseTest;
import com.esha.saucedemo.pages.LoginPage;
import com.esha.saucedemo.pages.ProductsPage;
import com.esha.saucedemo.utils.ConfigReader;
import com.esha.saucedemo.utils.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.esha.saucedemo.utils.ReportLogger.step;

/** Automates the Logout_Test_Cases sheet (TC_LOGOUT_001 - TC_LOGOUT_003). */
public class LogoutTests extends BaseTest {

    @Test(description = "TC_LOGOUT_001 - Verify successful logout",
            groups = {"smoke", "regression", "logout"})
    public void TC_LOGOUT_001_successfulLogout() {
        ProductsPage products = loginAsStandardUser();
        step("1. Open the navigation menu");
        step("2. Click Logout");
        products.logout();

        SoftAssert soft = new SoftAssert();
        soft.assertTrue(new LoginPage(driver).isLoginButtonDisplayed(), "Login page should be displayed");
        soft.assertEquals(driver.getCurrentUrl(), ConfigReader.get("baseUrl"), "URL should be the login page");
        soft.assertAll();
    }

    @Test(description = "TC_LOGOUT_002 - Verify protected page cannot be accessed after logout",
            groups = {"regression", "logout"})
    public void TC_LOGOUT_002_protectedPageBlockedAfterLogout() {
        ProductsPage products = loginAsStandardUser();
        products.logout();
        Assert.assertTrue(new LoginPage(driver).isLoginButtonDisplayed(), "Precondition: user has logged out");

        step("1. Attempt to access the Products page after logout");
        driver.get(ConfigReader.get("baseUrl") + "inventory.html");
        step("2. Observe the application behavior");

        LoginPage login = new LoginPage(driver);
        SoftAssert soft = new SoftAssert();
        soft.assertTrue(login.isLoginButtonDisplayed(), "Products page must not be reachable without authentication");
        soft.assertTrue(login.getErrorMessage().contains("logged in"), "An access-restricted message should be shown");
        soft.assertAll();
    }

    @Test(description = "TC_LOGOUT_003 - Verify user can log in again after logout",
            groups = {"regression", "logout"})
    public void TC_LOGOUT_003_loginAgainAfterLogout() {
        ProductsPage products = loginAsStandardUser();
        products.logout();
        Assert.assertTrue(new LoginPage(driver).isLoginButtonDisplayed(), "Precondition: user is on the Login page");

        step("1. Enter valid username");
        step("2. Enter valid password");
        step("3. Click Login");
        new LoginPage(driver).login(TestData.STANDARD_USER, TestData.PASSWORD);

        Assert.assertEquals(new ProductsPage(driver).getPageTitle(), "Products",
                "User should be redirected to the Products page");
    }
}
