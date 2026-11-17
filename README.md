# SauceDemo: Selenium Test Automation Framework

![Java](https://img.shields.io/badge/Java-17+-ED8B00?logo=openjdk&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-4.25-43B02A?logo=selenium&logoColor=white)
![TestNG](https://img.shields.io/badge/TestNG-7.10-FF6C37)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white)

A UI test automation framework for the [SauceDemo](https://www.saucedemo.com/) web store, built with **Java, Selenium WebDriver, TestNG and Maven** using the **Page Object Model**.
It automates **all 46 test cases** from my manual testing project, [saucedemo-manual-testing](https://github.com/EshaIsrarX01/saucedemo-manual-testing), one automated test per manual test case, with the same IDs, steps and expected results.

**Esha Israr** · SQA / Test Automation

---

## Highlights

- **46 of 46 manual test cases automated**, with the TC ID at the start of every test name and report entry
- **Page Object Model**: locators and page actions live in page classes, tests contain only the steps and checks
- **Explicit waits only**: no `Thread.sleep`
- **ExtentReports HTML report** (`ExtentReport.html`) with the manual steps of each test, screenshots on failure, and a dashboard by module
- **Known-defect handling**: `TC_CHECKOUT_010` follows the manual steps and fails because of **BUG_001**, with screenshots showing exactly where the application should have blocked the user
- **Three suites** (full, stable, smoke), headless mode, config file with command-line overrides, and a **GitHub Actions** workflow

## Results

| Suite | Tests | Expected result |
| --- | --- | --- |
| Full (`testng.xml`) | 46 | **45 pass, 1 fail** (TC_CHECKOUT_010, BUG_001), matching the manual execution report |
| Stable (`testng-stable.xml`) | 45 | all pass (known defect excluded) |
| Smoke (`testng-smoke.xml`) | 6 | all pass |

## BUG_001: the order can be placed with an empty cart

The application does not validate cart contents at checkout. A logged-in user with an empty cart can click Checkout, enter customer information, reach the Overview page showing **Item total $0 / Total $0.00**, click **Finish**, and receive the **"Thank you for your order!"** confirmation.

`TC_CHECKOUT_010` automates exactly that scenario. It asserts the **correct** behaviour (the user must be stopped and no order placed), so it **fails while the bug exists**. It uses soft assertions, so the whole flow is exercised and the report lists every point where the app should have stopped the user:

1. Step 3: Checkout must be blocked for an empty cart, but *Checkout: Your Information* opened
2. Step 4: the Overview page must not be reachable, but it showed a $0.00 total
3. Step 5: no order must be placed, but the confirmation was displayed

Each observation has a screenshot in the report. When the application is fixed, the test starts passing without any code change.

## Tech stack

| Area | Tool |
| --- | --- |
| Language | Java 17+ |
| Browser automation | Selenium WebDriver 4.25 (Selenium Manager handles the driver) |
| Test framework | TestNG 7.10 |
| Build | Maven (Surefire plugin) |
| Reporting | ExtentReports 5 (Spark reporter) |
| CI | GitHub Actions |

## Project structure

```
saucedemo-selenium-automation/
├── pom.xml
├── testng.xml            full suite (46 tests, includes the known-defect test)
├── testng-stable.xml     all tests except known defects (used by CI)
├── testng-smoke.xml      critical-path smoke tests
├── .github/workflows/ci.yml
└── src/
    ├── main/java/com/esha/saucedemo/
    │   ├── base/         BaseTest: browser setup and teardown, login helper
    │   ├── pages/        BasePage, LoginPage, ProductsPage, ProductDetailsPage, CartPage, Checkout*Page
    │   ├── listeners/    TestListener: report entries and failure screenshots
    │   └── utils/        ConfigReader, DriverFactory, ExtentManager, ReportLogger, ScreenshotUtil, TestData
    └── test/
        ├── java/com/esha/saucedemo/tests/   LoginTests, ProductTests, SortingTests, CartTests, CheckoutTests, LogoutTests
        └── resources/config.properties
```

## Design decisions

- **One automated test per manual test case**, named `TC_<MODULE>_<NNN>_<what>` so the report and the code follow the workbook exactly.
- **Page Object Model**: when SauceDemo's UI changes, only a page class changes, not every test.
- **`BaseTest` with `@BeforeMethod` / `@AfterMethod(alwaysRun = true)`**: each test gets a fresh browser, and the browser always closes, even when the test fails.
- **Explicit waits** (`WebDriverWait`) wrapped in `BasePage` helpers, and URL checks before reading a page title, so tests never read the previous page.
- **Soft assertions** where one manual test case checks several things (for example product information in the cart), so one run reports every mismatch instead of stopping at the first.
- **Manual steps are written into the report** with `ReportLogger.step(...)`, so each report entry reads like the manual test case.
- **Test data in one place** (`TestData`), settings in `config.properties`.

## Prerequisites

- JDK 17 or newer
- Maven (bundled with IntelliJ, or install it and use the `mvn` command)
- Google Chrome (the matching driver is downloaded automatically on first run)

## How to run

```bash
# Full suite: all 46 test cases (TC_CHECKOUT_010 fails on purpose because of BUG_001)
mvn clean test

# Stable suite: 45 test cases, known defect excluded
mvn clean test -DsuiteXmlFile=testng-stable.xml

# Smoke suite
mvn clean test -DsuiteXmlFile=testng-smoke.xml

# Headless Chrome (add to any command)
mvn clean test -Dheadless=true
```

In IntelliJ you can also right-click any `testng*.xml` file and choose **Run**.

## Reports

After a run, open **`target/extent-report/ExtentReport.html`** in a browser. It contains:

- pass / fail / skip totals and a dashboard, filterable by module (Login, Product, Sorting, Cart, Checkout, Logout) and by group (smoke, regression, known-defect)
- every test case with its steps, the result, and the error message when it failed
- a **screenshot embedded in each failed test**, plus step screenshots for the BUG_001 flow
- system information (tester, OS, Java version, browser)

Screenshots of failures are also saved as PNG files in `target/screenshots/`.

> Add a screenshot of your report here: `docs/extent-report.png`
>
> `![Extent report](docs/extent-report.png)`

## Traceability: all 46 manual test cases

| Test case | Module | What the automated test verifies | Priority | Expected suite result |
| --- | --- | --- | --- | --- |
| TC_LOGIN_001 | Login | successful login with valid credentials | High | Pass |
| TC_LOGIN_002 | Login | login fails with an invalid username | High | Pass |
| TC_LOGIN_003 | Login | login fails with an invalid password | High | Pass |
| TC_LOGIN_004 | Login | login validation when username is empty | High | Pass |
| TC_LOGIN_005 | Login | login validation when password is empty | High | Pass |
| TC_LOGIN_006 | Login | login is prevented for a locked user account | High | Pass |
| TC_LOGIN_007 | Login | password characters are masked | Medium | Pass |
| TC_LOGIN_008 | Login | login validation when both username and password are empty | High | Pass |
| TC_LOGIN_009 | Login | login using the Enter key | Medium | Pass |
| TC_PRODUCT_001 | Product | Products page is displayed after successful login | High | Pass |
| TC_PRODUCT_002 | Product | product listings are displayed | High | Pass |
| TC_PRODUCT_003 | Product | product names are displayed correctly and completely | Medium | Pass |
| TC_PRODUCT_004 | Product | product prices are displayed | Medium | Pass |
| TC_PRODUCT_005 | Product | product images are displayed | Medium | Pass |
| TC_PRODUCT_006 | Product | product descriptions are displayed | Low | Pass |
| TC_PRODUCT_007 | Product | Add to Cart button is available for products | High | Pass |
| TC_PRODUCT_008 | Product | navigation to product details page | Medium | Pass |
| TC_PRODUCT_009 | Product | user can return from product details to Products page | Medium | Pass |
| TC_PRODUCT_010 | Product | Products page is accessible after logging in again | Medium | Pass |
| TC_SORT_001 | Sorting | products can be sorted by Name (A to Z) | Medium | Pass |
| TC_SORT_002 | Sorting | products can be sorted by Name (Z to A) | Medium | Pass |
| TC_SORT_003 | Sorting | products can be sorted by Price (Low to High) | High | Pass |
| TC_SORT_004 | Sorting | products can be sorted by Price (High to Low) | High | Pass |
| TC_SORT_005 | Sorting | all sorting options are available | Medium | Pass |
| TC_CART_001 | Cart | a product can be added to the shopping cart | High | Pass |
| TC_CART_002 | Cart | cart badge count is updated after adding a product | High | Pass |
| TC_CART_003 | Cart | multiple products can be added to the cart | High | Pass |
| TC_CART_004 | Cart | shopping cart page can be opened | High | Pass |
| TC_CART_005 | Cart | product information is displayed correctly in the cart | Medium | Pass |
| TC_CART_006 | Cart | a product can be removed from the cart | High | Pass |
| TC_CART_007 | Cart | empty cart is displayed when no products are added | Medium | Pass |
| TC_CART_008 | Cart | Continue Shopping button navigation | Medium | Pass |
| TC_CART_009 | Cart | cart contents are retained when navigating between pages | Medium | Pass |
| TC_CHECKOUT_001 | Checkout | user can access checkout with products in cart | High | Pass |
| TC_CHECKOUT_002 | Checkout | checkout continues with valid customer information | High | Pass |
| TC_CHECKOUT_003 | Checkout | validation when first name is empty | High | Pass |
| TC_CHECKOUT_004 | Checkout | validation when last name is empty | High | Pass |
| TC_CHECKOUT_005 | Checkout | validation when postal code is empty | High | Pass |
| TC_CHECKOUT_006 | Checkout | order summary displays correct product and pricing information | High | Pass |
| TC_CHECKOUT_007 | Checkout | order can be completed using the Finish button | High | Pass |
| TC_CHECKOUT_008 | Checkout | order confirmation is displayed after successful checkout | High | Pass |
| TC_CHECKOUT_009 | Checkout | Cancel button returns user to the Cart page | Medium | Pass |
| TC_CHECKOUT_010 | Checkout | checkout cannot be initiated with an empty cart | High | **Fails: BUG_001** |
| TC_LOGOUT_001 | Logout | successful logout | High | Pass |
| TC_LOGOUT_002 | Logout | protected page cannot be accessed after logout | High | Pass |
| TC_LOGOUT_003 | Logout | user can log in again after logout | Medium | Pass |

## CI

The workflow in `.github/workflows/ci.yml` runs the stable suite in headless Chrome on every push and uploads the Extent report as a build artifact.

## Possible improvements

- Run tests in parallel and across browsers (Firefox, Edge)
- Selenium Grid or Docker execution
- Retry analyzer for flaky tests
- Publish the report with GitHub Pages
- Extend coverage to SauceDemo's other user profiles (`problem_user`, `error_user`, `visual_user`, `performance_glitch_user`)

## Contact

Esha Israr · [LinkedIn](https://www.linkedin.com/in/eshaisrar08) · [Email](mailto:eesha.israr08@gmail.com) · [GitHub](https://github.com/EshaIsrarX01)
