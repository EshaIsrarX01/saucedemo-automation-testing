package com.esha.saucedemo.utils;

import java.util.List;

/** Central place for test data, so tests never contain "magic" strings. Values match the Test Cases workbook. */
public final class TestData {

    private TestData() {
    }

    // Accounts published on the SauceDemo login page
    public static final String STANDARD_USER = "standard_user";
    public static final String LOCKED_USER = "locked_out_user";
    public static final String PASSWORD = "secret_sauce";

    // Products
    public static final String BACKPACK = "Sauce Labs Backpack";
    public static final String BIKE_LIGHT = "Sauce Labs Bike Light";
    public static final String BACKPACK_PRICE_TEXT = "$29.99";
    public static final double BACKPACK_PRICE = 29.99;
    public static final int PRODUCT_COUNT = 6;
    public static final List<String> PRODUCT_NAMES = List.of(
            "Sauce Labs Backpack",
            "Sauce Labs Bike Light",
            "Sauce Labs Bolt T-Shirt",
            "Sauce Labs Fleece Jacket",
            "Sauce Labs Onesie",
            "Test.allTheThings() T-Shirt (Red)");

    // Checkout customer information
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";
    public static final String ZIP_CODE = "12345";

    // SauceDemo's sales tax rate and confirmation text
    public static final double TAX_RATE = 0.08;
    public static final String CONFIRMATION_MESSAGE = "Thank you for your order!";
}
