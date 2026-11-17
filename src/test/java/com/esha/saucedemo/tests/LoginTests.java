package com.esha.saucedemo.tests;

import com.esha.saucedemo.base.BaseTest;
import com.esha.saucedemo.pages.LoginPage;
import com.esha.saucedemo.pages.ProductsPage;
import com.esha.saucedemo.utils.ReportLogger;
import com.esha.saucedemo.utils.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.esha.saucedemo.utils.ReportLogger.step;

/** Automates the Login_Test_Cases sheet (TC_LOGIN_001 - TC_LOGIN_009). */
public class LoginTests extends BaseTest {

    /** Common expected result for every negative case: error shown, user stays on the Login page. */
    private void assertLoginRejected(LoginPage login, String expectedErrorFragment) {
        String actual = login.getErrorMessage();
        ReportLogger.stepWithScreenshot(driver, "Observed error message: " + actual);
        Assert.assertTrue(actual.contains(expectedErrorFragment),
                "Expected error containing '" + expectedErrorFragment + "' but was '" + actual + "'");
        Assert.assertTrue(login.isLoginButtonDisplayed(), "User should remain on the Login page");
        Assert.assertFalse(driver.getCurrentUrl().contains("inventory"), "User must not reach the Products page");
    }

    @Test(description = "TC_LOGIN_001 - Verify successful login with valid credentials",
            groups = {"smoke", "regression", "login"})
    public void TC_LOGIN_001_validLogin() {
        LoginPage login = new LoginPage(driver);
        step("1. Enter a valid username: " + TestData.STANDARD_USER);
        login.enterUsername(TestData.STANDARD_USER);
        step("2. Enter a valid password");
        login.enterPassword(TestData.PASSWORD);
        step("3. Click the Login button");
        login.clickLogin();

        Assert.assertEquals(new ProductsPage(driver).getPageTitle(), "Products",
                "User should be redirected to the Products page");
    }

    @Test(description = "TC_LOGIN_002 - Verify login fails with an invalid username",
            groups = {"regression", "login"})
    public void TC_LOGIN_002_invalidUsername() {
        LoginPage login = new LoginPage(driver);
        step("1. Enter an invalid username: invalid_user");
        login.enterUsername("invalid_user");
        step("2. Enter a valid password");
        login.enterPassword(TestData.PASSWORD);
        step("3. Click the Login button");
        login.clickLogin();

        assertLoginRejected(login, "Username and password do not match");
    }

    @Test(description = "TC_LOGIN_003 - Verify login fails with an invalid password",
            groups = {"regression", "login"})
    public void TC_LOGIN_003_invalidPassword() {
        LoginPage login = new LoginPage(driver);
        step("1. Enter a valid username");
        login.enterUsername(TestData.STANDARD_USER);
        step("2. Enter an invalid password: wrong_password");
        login.enterPassword("wrong_password");
        step("3. Click the Login button");
        login.clickLogin();

        assertLoginRejected(login, "Username and password do not match");
    }

    @Test(description = "TC_LOGIN_004 - Verify login validation when username is empty",
            groups = {"regression", "login"})
    public void TC_LOGIN_004_emptyUsername() {
        LoginPage login = new LoginPage(driver);
        step("1. Leave the username field empty");
        step("2. Enter a valid password");
        login.enterPassword(TestData.PASSWORD);
        step("3. Click the Login button");
        login.clickLogin();

        assertLoginRejected(login, "Username is required");
    }

    @Test(description = "TC_LOGIN_005 - Verify login validation when password is empty",
            groups = {"regression", "login"})
    public void TC_LOGIN_005_emptyPassword() {
        LoginPage login = new LoginPage(driver);
        step("1. Enter a valid username");
        login.enterUsername(TestData.STANDARD_USER);
        step("2. Leave the password field empty");
        step("3. Click the Login button");
        login.clickLogin();

        assertLoginRejected(login, "Password is required");
    }

    @Test(description = "TC_LOGIN_006 - Verify login is prevented for a locked user account",
            groups = {"regression", "login"})
    public void TC_LOGIN_006_lockedUser() {
        LoginPage login = new LoginPage(driver);
        step("1. Enter the locked account username: " + TestData.LOCKED_USER);
        login.enterUsername(TestData.LOCKED_USER);
        step("2. Enter the valid password");
        login.enterPassword(TestData.PASSWORD);
        step("3. Click the Login button");
        login.clickLogin();

        assertLoginRejected(login, "locked out");
    }

    @Test(description = "TC_LOGIN_007 - Verify password characters are masked",
            groups = {"regression", "login"})
    public void TC_LOGIN_007_passwordMasked() {
        LoginPage login = new LoginPage(driver);
        step("1. Click the password field and enter a password");
        login.enterPassword(TestData.PASSWORD);
        step("2. Observe the password field");

        Assert.assertEquals(login.getPasswordFieldType(), "password",
                "Password field must be of type 'password' so characters are masked");
    }

    @Test(description = "TC_LOGIN_008 - Verify login validation when both username and password are empty",
            groups = {"regression", "login"})
    public void TC_LOGIN_008_bothFieldsEmpty() {
        LoginPage login = new LoginPage(driver);
        step("1. Leave the username field empty");
        step("2. Leave the password field empty");
        step("3. Click the Login button");
        login.clickLogin();

        assertLoginRejected(login, "Username is required");
    }

    @Test(description = "TC_LOGIN_009 - Verify login using the Enter key",
            groups = {"regression", "login"})
    public void TC_LOGIN_009_loginWithEnterKey() {
        LoginPage login = new LoginPage(driver);
        step("1. Enter a valid username");
        step("2. Enter a valid password");
        step("3. Press the Enter key");
        login.loginWithEnterKey(TestData.STANDARD_USER, TestData.PASSWORD);

        Assert.assertEquals(new ProductsPage(driver).getPageTitle(), "Products",
                "User should be redirected to the Products page using the Enter key");
    }
}
