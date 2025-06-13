package com.browserstack.tests;

import org.openqa.selenium.JavascriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import utils.DriverManager;

public class SeleniumTest {
    private static final Logger log = LoggerFactory.getLogger(SeleniumTest.class);
    public static WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    @SuppressWarnings("unchecked")
    public void beforeSuite() {
        log.info("Starting Selenium Test Suite");
        String browser = "chrome";
        DriverManager.setDriverBrowser(browser);
    }

    @BeforeMethod(alwaysRun = true, firstTimeOnly = true)
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        driver = DriverManager.getInstance().getDriver();
//        clearBrowserData();

        }

    @AfterMethod(lastTimeOnly = true, onlyForGroups =  {"ViewCartTest", "ChooseItemVariations", "BStackDemoTest", "BStackLocalTest", "ClearCookies"})
    public void clearCookies() throws Exception {
//        clearBrowserData();
    }

    @AfterMethod(alwaysRun = true, lastTimeOnly = true, onlyForGroups = {"clearCookies"})
    public void tearDown() throws Exception {
        driver = DriverManager.getInstance().getDriver();
        driver.quit();
    }


    protected void clearBrowserData() {
        if (driver != null) {
            // Clear cookies
            driver.manage().deleteAllCookies();
            // Clear cache and local storage using JavaScript
            log.info(
                    "Clearing browser data: cookies, local storage, and session storage."
            );
            try {
                // Clear local storage
                ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
                // Clear session storage
                ((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");
            } catch (Exception e) {
                // Ignore exceptions that might occur if no page is loaded yet
            }
        }
    }

    // To quit driver after all tests run
    // This will run only once, after all tests ran
    // Another advantage of this is, it will quit driver even though you stop the program manually
//    static {
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            DriverManager.getInstance().getDriver().quit();
//        }));
//    }

}
