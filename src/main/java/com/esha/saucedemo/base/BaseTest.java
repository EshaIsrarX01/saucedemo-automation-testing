package com.esha.saucedemo.base;

import com.esha.saucedemo.listeners.TestListener;
import com.esha.saucedemo.pages.LoginPage;
import com.esha.saucedemo.pages.ProductsPage;
import com.esha.saucedemo.utils.ConfigReader;
import com.esha.saucedemo.utils.DriverFactory;
import com.esha.saucedemo.utils.TestData;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

/** Every test class extends this. It opens a fresh browser before each test and always closes it afterwards. */
@Listeners(TestListener.class)
public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.createDriver();
        driver.get(ConfigReader.get("baseUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    /** Precondition helper used by most tests: log in and confirm the Products page loaded. */
    protected ProductsPage loginAsStandardUser() {
        new LoginPage(driver).login(TestData.STANDARD_USER, TestData.PASSWORD);
        ProductsPage products = new ProductsPage(driver);
        Assert.assertEquals(products.getPageTitle(), "Products", "Precondition failed: login did not reach Products page");
        return products;
    }
}
