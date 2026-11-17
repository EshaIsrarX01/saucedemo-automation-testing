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
