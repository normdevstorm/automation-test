package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class OrderShippingAndPaymentPage {
    private static final Logger log = LoggerFactory.getLogger(OrderShippingAndPaymentPage.class);
    private static final long SHORT_TIMEOUT = 15;
    private final WebDriver webDriver;
    private final WebDriverWait wait;

    // Billing fields
    @FindBy(id = "billing_last_name")
    private WebElement recipientNameInput;

    @FindBy(id = "billing_phone")
    private WebElement phoneNumberInput;

    @FindBy(id = "billing_email")
    private WebElement emailInput;

    @FindBy(id = "billing_address_1")
    private WebElement addressInput;

    @FindBy(id = "billing_state")
    private WebElement stateDropdown;

    @FindBy(id = "billing_city")
    private WebElement cityDropdown;

    @FindBy(id = "billing_address_2")
    private WebElement districtDropdown;

    @FindBy(id = "order_comments")
    private WebElement orderCommentsInput;

    @FindBy(id = "createaccount")
    private WebElement createAccountCheckbox;

    // Payment methods
    @FindBy(id = "payment_method_cod")
    private WebElement codPaymentOption;

    @FindBy(id = "payment_method_bacs")
    private WebElement bankTransferPaymentOption;

    // Terms and conditions
    @FindBy(id = "terms")
    private WebElement termsCheckbox;

    // Place order button
    @FindBy(id = "place_order")
    private WebElement placeOrderButton;

    // Order confirmation elements
    @FindBy(className = "woocommerce-order-received")
    private WebElement orderConfirmation;

    public OrderShippingAndPaymentPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));
    }

    /**
     * Fill in all required billing information
     */
    public void fillBillingInformation(String name, String phone, String email, String address) {
        log.info("Filling billing information");
        wait.until(ExpectedConditions.visibilityOf(recipientNameInput)).sendKeys(name);
        phoneNumberInput.sendKeys(phone);
        emailInput.sendKeys(email);
        addressInput.sendKeys(address);
    }

    /**
     * Select state/province from dropdown
     */
    public void selectState(String stateValue) {
        log.info("Selecting state/province: {}", stateValue);
        Select stateSelect = new Select(wait.until(ExpectedConditions.visibilityOf(stateDropdown)));
        selectByTextContains(stateSelect, stateValue);
    }

    /**
     * Select city from dropdown
     */
    public void selectDistrict(String districtValue) {
        log.info("Selecting district: {}", districtValue);
        Select districtSelect = new Select(wait.until(ExpectedConditions.visibilityOf(cityDropdown)));
        waitForElementToBeInteractive(districtSelect.getWrappedElement());
        selectByTextContains(districtSelect, districtValue);
    }

    /**
     * Select district from dropdown
     */
    public void selectCommune(String communeValue) {
        log.info("Selecting commune: {}", communeValue);
        Select communeSelect = new Select(wait.until(ExpectedConditions.visibilityOf(districtDropdown)));
        waitForElementToBeInteractive(communeSelect.getWrappedElement());
        selectByTextContains(communeSelect, communeValue);
    }

    /**
     * Helper method to select an option by text that contains the given value
     *
     * @param select        The Select element
     * @param textToContain Text that should be contained in the option
     */
    private void selectByTextContains(Select select, String textToContain) {
        List<WebElement> matchingOptions = select.getOptions().stream()
                .filter(option -> option.getText().contains(textToContain))
                .collect(Collectors.toList());

        if (matchingOptions.isEmpty()) {
            log.warn("No option found containing text: {}", textToContain);
            throw new RuntimeException("No dropdown option found containing: " + textToContain);
        } else {
            matchingOptions.get(0).click();
            log.debug("Selected option containing text: {}", textToContain);
        }
    }

    /**
     * Toggle create account option
     */
    public void toggleCreateAccount(boolean create) {
        log.info("Setting create account option: {}", create);
        if ((createAccountCheckbox.isSelected() && !create) ||
                (!createAccountCheckbox.isSelected() && create)) {
            wait.until(ExpectedConditions.elementToBeClickable(createAccountCheckbox)).click();
        }
    }

    /**
     * Select payment method
     *
     * @param method - "cod", "bank", "vnpay", or "momo"
     */
    public void selectPaymentMethod(String method) {
        log.info("Selecting payment method: {}", method);
        switch (method.toLowerCase()) {
            case "cod":
                wait.until(ExpectedConditions.elementToBeClickable(codPaymentOption));
                if(!codPaymentOption.isSelected()){
                    ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", codPaymentOption);

                }
                break;
            case "bank":
                wait.until(ExpectedConditions.elementToBeClickable(bankTransferPaymentOption));
                if(!bankTransferPaymentOption.isSelected()){
                    ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", bankTransferPaymentOption);
                }
                break;
            default:
                log.warn("Unknown payment method: {}", method);
        }
    }

    /**
     * Add order comments
     */
    public void addOrderComments(String comments) {
        log.info("Adding order comments");
        wait.until(ExpectedConditions.visibilityOf(orderCommentsInput)).sendKeys(comments);
    }

    /**
     * Accept terms and conditions
     */
    public void acceptTermsAndConditions() {
        log.info("Accepting terms and conditions");
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", termsCheckbox);
    }

    /**
     * Place the order
     */
    public void placeOrder() {
        log.info("Placing order");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".blockUI.blockOverlay")));
        wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton)).click();
//        wait.until(ExpectedConditions.visibilityOf(orderConfirmation));
        log.info("Order placed successfully");
    }

    /**
     * Complete checkout with all required information
     */
    public void completeCheckout(String name, String phone, String email, String address,
                                 String paymentMethod, String comments) {
        fillBillingInformation(name, phone, email, address);
        addOrderComments(comments);
        selectPaymentMethod(paymentMethod);
        acceptTermsAndConditions();
        placeOrder();
    }

    /**
     * Complete checkout with full address information
     */
    public void completeCheckoutWithFullAddress(String name, String phone, String email,
                                                String state, String district,
                                                String commune, String address,
                                                String paymentMethod, String comments) {
        fillBillingInformation(name, phone, email, address);
        selectState(state);
        selectDistrict(district);
        selectCommune(commune);
        addOrderComments(comments);
        selectPaymentMethod(paymentMethod);
        acceptTermsAndConditions();

        Assert.assertTrue(stateDropdown.getText().contains(state));
        Assert.assertTrue(cityDropdown.getText().contains(district));
        Assert.assertTrue(districtDropdown.getText().contains(commune));
        Assert.assertTrue(orderCommentsInput.getAttribute("value").contains(comments));        Assert.assertTrue(Objects.equals(paymentMethod, "bank") ? bankTransferPaymentOption.isSelected() : codPaymentOption.isSelected());
        Assert.assertTrue(termsCheckbox.isSelected());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        placeOrder();
    }

    /**
     * Verify successful order placement
     */
    public boolean isOrderConfirmed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(orderConfirmation)).isDisplayed();
        } catch (Exception e) {
            log.error("Order confirmation not found", e);
            return false;
        }
    }

    private void waitForElementToBeInteractive(WebElement element) {
        try {
            // First wait for it to be visible
            wait.until(ExpectedConditions.visibilityOf(element));

            // Then wait a bit more for any AJAX requests to complete
            Duration pollInterval = Duration.ofMillis(200);
            WebDriverWait shortWait = new WebDriverWait(webDriver, Duration.ofSeconds(SHORT_TIMEOUT), pollInterval);
            shortWait.until(driver -> {
                try {
                    return element.isEnabled() && element.isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (Exception e) {
            log.warn("Element may not be fully interactive yet: {}", element, e);
        }
    }
}

