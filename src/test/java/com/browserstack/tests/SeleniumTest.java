package com.browserstack.tests;

import org.openqa.selenium.JavascriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SeleniumTest {
    private static final Logger log = LoggerFactory.getLogger(SeleniumTest.class);
    public WebDriver driver;

    @BeforeMethod(alwaysRun = true, firstTimeOnly = true)
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
//        System.setProperty("webdriver.chrome.driver", "/run/media/normdevstorm/data-linux/UNI/AUTOMATED_TESTING/chromedriver-linux64/chromedriver");
        ChromeOptions options = new ChromeOptions();
        // Add your custom profile
        options.addArguments("user-data-dir=/home/normdevstorm/.config/chromium/Default");

        // Add memory-efficient options
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("start-maximized");
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        clearBrowserData();
    }

    @AfterMethod(lastTimeOnly = true, onlyForGroups =  {"ViewCartTest", "ChooseItemVariations", "BStackDemoTest", "BStackLocalTest", "ClearCookies"})
    public void clearCookies() throws Exception {
        clearBrowserData();
    }

    @AfterMethod(alwaysRun = true, lastTimeOnly = true, dependsOnMethods = {"clearCookies"})
    public void tearDown() throws Exception {
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

}
