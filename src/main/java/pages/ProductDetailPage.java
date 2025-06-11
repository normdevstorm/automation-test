package pages;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.WebUI;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDetailPage {

    private static final Logger log = LoggerFactory.getLogger(ProductDetailPage.class);
    WebDriver webDriver;
    Actions actions;

    @FindBy(xpath = "//form/table/tbody/tr[1]/td[2]/ul")
    private WebElement attributeSize;
    @FindBy(className = "rey-qtyField")
    private WebElement inputQuantity;
    @FindBy(xpath = "//form/div/div[2]/div/button")
    private WebElement buttonAddToCart;
    @FindBy(xpath = "/html/body/div[12]/div[2]/div[2]/div/div[2]/a[3]")
    private WebElement checkoutDialog;
    @FindBy(className = "rey-acPopup-buttons-cart")
    private WebElement viewCartButton;
    @FindBy(xpath = "//form/table/tbody/tr[2]/td[2]/ul")
    private WebElement attributeColor;
    @FindBy(className = "reset_variations")
    private WebElement resetVariationsButton;
    @FindBy(className = "rey-modalClose")
    private WebElement closeCheckoutDialogButton;


    public ProductDetailPage(WebDriver webDriver){
        this.webDriver = webDriver;
        this.actions = new Actions(webDriver);
        PageFactory.initElements(webDriver, this);
    }

    public void openProductDetailPage(String productUrl) {
        webDriver.get(productUrl);
        log.info("Open product detail page: {}", productUrl);
        Assert.assertTrue(webDriver.getCurrentUrl().contains(productUrl), "The product detail page is not opened correctly.");
        Allure.step("Open product detail page: " + productUrl);
    }

    public void selectNumberOfItems(){

    }

    // Pass test data to this method
    public void addCardFluentActionsTest(String size, String quantity, String color) {
       WebDriverWait wait =  new WebDriverWait(webDriver, Duration.ofSeconds(20));

       wait.until(
                webDriver1 -> ((JavascriptExecutor) webDriver1)
                        .executeScript("return document.readyState").equals("complete")
       );

        if (!webDriver.findElements(By.xpath("//form/table/tbody/tr[1]/td[2]/ul")).isEmpty()) {
            chooseSizeAttribute(size);
       }

        if (!webDriver.findElements(By.xpath("//form/table/tbody/tr[2]/td[2]/ul")).isEmpty()) {
            chooseColorAttribute(color);
        }

        setItemQuantity(quantity);
    }

    public void setItemQuantity(String quantity) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(inputQuantity));
        scrollToElement(inputQuantity);
        Assert.assertTrue(inputQuantity.isDisplayed(), "The input quantity is not displayed!!!");
        WebElement inputQuantityTarget = inputQuantity.findElement(By.tagName("input"));
        inputQuantityTarget.clear();
        inputQuantityTarget.sendKeys(quantity);
        log.info("Set item quantity to: {}", quantity);
        Allure.step("Set item quantity to: " + quantity);
    }

    public ViewCartPage navigateToViewCartPage(){
        WebDriverWait wait =  new WebDriverWait(webDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(checkoutDialog));
        scrollToElement(viewCartButton);
        viewCartButton.click();
        return new ViewCartPage(webDriver);
    }

     private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.warn("Scroll pause was interrupted", e);
        }
    }

    private void chooseColorAttribute(String color) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(attributeColor));
        List<WebElement> colorOptions = attributeColor.findElements(By.tagName("li"));
        WebElement targetColorOption = colorOptions.stream()
                .filter(webElement -> webElement.getAttribute("data-title").contains(color))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Color option not found: " + color));
        targetColorOption.click();
        if(!targetColorOption.isSelected()){
            targetColorOption.click();
        }

        Assert.assertTrue(targetColorOption.getAttribute("class").contains("selected"), "The color option is not selected!");
        Assert.assertTrue(targetColorOption.getAttribute("data-title").contains(color), "The expected color and the actual are not matched with each other!!!");
        Allure.step("Choose color attribute: " + color);
    }

    private void chooseSizeAttribute(String size) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(attributeSize));
        List<WebElement> sizeOptions = attributeSize.findElements(By.tagName("li"));
        WebElement targetSizeOption = sizeOptions.stream()
                .filter(webElement -> webElement.getAttribute("data-title").contains(size))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Color option not found: " + size));
        scrollToElement(targetSizeOption);
        targetSizeOption.click();
        if(!targetSizeOption.isSelected()){
            targetSizeOption.click();
        }
        Assert.assertTrue(targetSizeOption.getAttribute("class").contains("selected"));
        Assert.assertTrue(targetSizeOption.getAttribute("data-title").contains(size), "The expected size and the actual are not matched with each other!!!");
        Allure.step("Choose size attribute: " + size);

    }
    public void resetVariations() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(resetVariationsButton));
        scrollToElement(resetVariationsButton);
        resetVariationsButton.click();
        log.info("Reset variations button clicked");
        Allure.step("Reset variations");
    }

    public void clickAddToCartButton() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(buttonAddToCart)).click();
        log.info("Add to cart button clicked");
        Allure.step("Click Add to Cart button");
        wait.until(ExpectedConditions.visibilityOf(checkoutDialog));
    }

    public void closeCheckoutDialog() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(closeCheckoutDialogButton)).click();
        log.info("Checkout dialog closed");
        Allure.step("Close checkout dialog");
    }

    private void handleAlertAccept() {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.alertIsPresent());
            String alertText = webDriver.switchTo().alert().getText();
            log.info("Alert text: {}", alertText);
            webDriver.switchTo().alert().accept();
            Allure.step("Alert accepted with text: " + alertText);
        } catch (Exception e) {
            log.warn("No alert to handle", e);
        }
    }



    public void addToCartFailedAfterResetVariations(String url, String size, String quantity, String color) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        openProductDetailPage(url);
//        WebUI.waitForPageLoaded();
        addCardFluentActionsTest(size, quantity, color);
        resetVariations();
        clickAddToCartButton();
        Assert.assertNotNull(wait.until(ExpectedConditions.alertIsPresent()), "Alert is not present!");
        handleAlertAccept();
    }

    public void addToCartWithZeroQuantity(String url, String size, String color) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        openProductDetailPage(url);
        addCardFluentActionsTest(size, "0", color);
        clickAddToCartButton();
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        Boolean isValid = (Boolean) js.executeScript("return arguments[0].checkValidity();", inputQuantity.findElement(By.tagName("input")));
        String validationMessage = (String) js.executeScript("return arguments[0].validationMessage;", inputQuantity.findElement(By.tagName("input")));
        log.info("Validation message: {}", validationMessage);
        Assert.assertFalse(isValid);
        Assert.assertEquals(validationMessage, "Value must be greater than or equal to 1.");
//        log.info(tooltipText);
    }

        public void addToCartWithInvalidQuantity(String url, String size, String color, String invalidQuantity) {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            openProductDetailPage(url);
            addCardFluentActionsTest(size, invalidQuantity, color);
            clickAddToCartButton();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Assert.assertTrue(webDriver.findElements(By.xpath("/html/body/div[12]/div[2]/div[2]/div/div[2]/a[3]")).isEmpty());

            Allure.step("Attempted to add to cart with invalid quantity: " + invalidQuantity);
        }
}
