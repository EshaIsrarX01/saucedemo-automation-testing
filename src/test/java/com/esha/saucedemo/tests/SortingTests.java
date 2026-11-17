package com.esha.saucedemo.tests;

import com.esha.saucedemo.base.BaseTest;
import com.esha.saucedemo.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.esha.saucedemo.utils.ReportLogger.step;

/** Automates the Sorting_Test_Cases sheet (TC_SORT_001 - TC_SORT_005). */
public class SortingTests extends BaseTest {

    @Test(description = "TC_SORT_001 - Verify products can be sorted by Name (A to Z)",
            groups = {"regression", "sorting"})
    public void TC_SORT_001_nameAscending() {
        ProductsPage products = loginAsStandardUser();
        step("1. Open the sorting dropdown");
        step("2. Select Name (A to Z)");
        products.sortBy("Name (A to Z)");
        step("3. Observe the product names");

        List<String> actual = products.getProductNames();
        List<String> expected = new ArrayList<>(actual);
        Collections.sort(expected);
        Assert.assertEquals(actual, expected, "Products are not in ascending alphabetical order");
    }

    @Test(description = "TC_SORT_002 - Verify products can be sorted by Name (Z to A)",
            groups = {"regression", "sorting"})
    public void TC_SORT_002_nameDescending() {
        ProductsPage products = loginAsStandardUser();
        step("1. Open the sorting dropdown");
        step("2. Select Name (Z to A)");
        products.sortBy("Name (Z to A)");
        step("3. Observe the product names");

        List<String> actual = products.getProductNames();
        List<String> expected = new ArrayList<>(actual);
        expected.sort(Collections.reverseOrder());
        Assert.assertEquals(actual, expected, "Products are not in descending alphabetical order");
    }

    @Test(description = "TC_SORT_003 - Verify products can be sorted by Price (Low to High)",
            groups = {"regression", "sorting"})
    public void TC_SORT_003_priceAscending() {
        ProductsPage products = loginAsStandardUser();
        step("1. Open the sorting dropdown");
        step("2. Select Price (low to high)");
        products.sortBy("Price (low to high)");
        step("3. Observe the product prices");

        List<Double> actual = products.getProductPrices();
        List<Double> expected = new ArrayList<>(actual);
        Collections.sort(expected);
        Assert.assertEquals(actual, expected, "Prices are not in ascending order");
    }

    @Test(description = "TC_SORT_004 - Verify products can be sorted by Price (High to Low)",
            groups = {"regression", "sorting"})
    public void TC_SORT_004_priceDescending() {
        ProductsPage products = loginAsStandardUser();
        step("1. Open the sorting dropdown");
        step("2. Select Price (high to low)");
        products.sortBy("Price (high to low)");
        step("3. Observe the product prices");

        List<Double> actual = products.getProductPrices();
        List<Double> expected = new ArrayList<>(actual);
        expected.sort(Collections.reverseOrder());
        Assert.assertEquals(actual, expected, "Prices are not in descending order");
    }

    @Test(description = "TC_SORT_005 - Verify all sorting options are available",
            groups = {"regression", "sorting"})
    public void TC_SORT_005_allSortOptionsAvailable() {
        ProductsPage products = loginAsStandardUser();
        step("1. Open the sorting dropdown");
        step("2. Observe the available options");

        Assert.assertEquals(products.getSortOptions(), Arrays.asList(
                "Name (A to Z)", "Name (Z to A)", "Price (low to high)", "Price (high to low)"));
    }
}
