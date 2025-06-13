package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverManager {
    // ThreadLocal to manage WebDriver instances per thread
    private static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

    // Volatile keyword ensures visibility of changes to the WebDriverSingleton instance across threads
    private static volatile DriverManager instance;

    public static String getDriverBrowser() {
        return driverBrowser;
    }

    public static void setDriverBrowser(String driverBrowser) {
        DriverManager.driverBrowser = driverBrowser;
    }

    private static String driverBrowser = "chrome"; // Default browser is set to Chrome

    // Private constructor to prevent instantiation
    private DriverManager() {}

    /**
     * Method to get the singleton instance of WebDriverSingleton.
     * Uses double-checked locking for thread-safe initialization.
     */
    public static DriverManager getInstance() {
        if (instance == null) { // First check without synchronization for performance
            synchronized (DriverManager.class) {  // Class-level locking for thread safety
                if (instance == null) { // Second check inside synchronized block for proper lazy initialization
                    instance = new DriverManager(); // Create the singleton instance
                }
            }
        }
        return instance;
    }

    /**
     * Sets the WebDriver instance for the current thread based on the specified browser.
     * If already set, it will return the existing instance.
     */
    public WebDriver getDriver() {
        if (threadLocalDriver.get() == null) {
            // Call setDriver to create and set a WebDriver instance for the thread
            threadLocalDriver.set(setDriver());
        }
        return threadLocalDriver.get(); // Return the WebDriver instance
    }

    /**
     * Creates and configures a new WebDriver instance based on the specified browser.
     */
    private WebDriver setDriver() {
        WebDriver driver;
        switch (driverBrowser.toLowerCase()) {
            case "chrome":
                ChromeOptions options = new ChromeOptions();
//                 Add your custom profile
//                options.addArguments("user-data-dir=/home/normdevstorm/.config/chromium/Default");

//                 Add memory-efficient options
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--no-sandbox");
                options.addArguments("start-maximized");
                options.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(options);
                break;
            case "firefox":
                driver = new FirefoxDriver(); // Selenium 4+ auto-detects driver paths
                break;
            case "edge":
                driver = new EdgeDriver(); // Selenium 4+ auto-detects driver paths
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + driverBrowser);
        }
        driver.manage().window().maximize();
        return driver; // Return the newly created driver
    }

    /**
     * Quits the WebDriver for the current thread and removes it from ThreadLocal storage.
     */
    public void quitDriver() {
        WebDriver driver = threadLocalDriver.get();
        if (driver != null) {
            driver.quit(); // Quit the WebDriver instance
            threadLocalDriver.remove(); // Remove the WebDriver instance from ThreadLocal
        }
    }
}