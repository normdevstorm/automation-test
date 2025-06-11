package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TestUtils {

    private static final Logger log = LoggerFactory.getLogger(TestUtils.class);

    static public void scrollToElement(WebDriver webDriver, WebElement element, Thread thread) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        try {
           thread.wait(2000);
        } catch (InterruptedException e) {
            log.warn("Scroll pause was interrupted", e);
        }
    }
}
