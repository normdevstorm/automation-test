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





    // To quit driver after all tests run
    // This will run only once, after all tests ran
    // Another advantage of this is, it will quit driver even though you stop the program manually
//    @AfterSuite(alwaysRun = true)
//    public void quitDriver() {
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            DriverManager.getInstance().getDriver().quit();
//        }));
//    }

}
