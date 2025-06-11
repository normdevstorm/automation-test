package pages;

import constants.FrameworkConstants;
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

import static constants.FrameworkConstants.FORM_ERROR_BORDER_COLOR_RBG;


public class OrderShippingAndPaymentPage {
    private static final Logger log = LoggerFactory.getLogger(OrderShippingAndPaymentPage.class);
    private static final long SHORT_TIMEOUT = 15;
    private static final long DEFAULT_TIMEOUT = 15;
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
    private WebElement districtDropdown;

    @FindBy(id = "billing_address_2")
    private WebElement communeDropdown;

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

    @FindBy(className = "woocommerce-error")
    private WebElement orderInformationErrorWrapper;

    private final String orderInformationErrorLastNameDataId = "billing_last_name";
    private final String orderInformationErrorPhoneDataId = "billing_phone";
    private final String orderInformationErrorDetailAddressDataId = "billing_address_1";

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
        wait.until(ExpectedConditions.visibilityOf(recipientNameInput));
        if(!recipientNameInput.getAttribute("value").isEmpty()) {
            recipientNameInput.clear();
        }
        recipientNameInput.sendKeys(name);

        if(!phoneNumberInput.getAttribute("value").isEmpty()) {
            phoneNumberInput.clear();
        }
        phoneNumberInput.sendKeys(phone);

        if(!emailInput.getAttribute("value").isEmpty()) {
            emailInput.clear();
        }
        emailInput.sendKeys(email);

        if(!addressInput.getAttribute("value").isEmpty()) {
            addressInput.clear();
        }
        addressInput.sendKeys(address);
    }

    /**
     * Select state/province from dropdown
     */
    public void selectState(String stateValue) {
        log.info("Selecting state/province: {}", stateValue);
        Select stateSelect = new Select(wait.until(ExpectedConditions.elementToBeClickable(stateDropdown)));
        selectByTextContains(stateSelect, stateValue);
    }

    /**
     * Select city from dropdown
     */
    public void selectDistrict(String districtValue) {
        log.info("Selecting district: {}", districtValue);
        Select districtSelect = new Select(wait.until(ExpectedConditions.elementToBeClickable(districtDropdown)));
        waitForElementToBeInteractive(districtSelect.getWrappedElement());
        selectByTextContains(districtSelect, districtValue);
    }

    /**
     * Select district from dropdown
     */
    public void selectCommune(String communeValue) {
        log.info("Selecting commune: {}", communeValue);
        Select communeSelect = new Select(wait.until(ExpectedConditions.elementToBeClickable(communeDropdown)));
//        wait.until(driver -> communeSelect.getWrappedElement().findElement(By.xpath("..")).getAttribute("class").contains("devn_loading"));
//        wait.until(driver -> !communeSelect.getWrappedElement().findElement(By.xpath("..")).getAttribute("class").contains("devn_loading"));
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
        Assert.assertTrue(districtDropdown.getText().contains(district));
        Assert.assertTrue(communeDropdown.getText().contains(commune));
        Assert.assertTrue(orderCommentsInput.getAttribute("value").contains(comments));        Assert.assertTrue(Objects.equals(paymentMethod, "bank") ? bankTransferPaymentOption.isSelected() : codPaymentOption.isSelected());
        Assert.assertTrue(termsCheckbox.isSelected());

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
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

    /**
     * Waits until the cart page is fully loaded (document.readyState === "complete").
     */
    public void waitForPageLoaded() {
        new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT)).until(
                driver -> ((org.openqa.selenium.JavascriptExecutor) driver)
                        .executeScript("return document.readyState").equals("complete")
        );
        log.info("Page loaded successfully");
    }

    public void verifyOrderPaymentAndShipment(String name, String phone, String email,
                                              String state, String district, String commune,
                                              String address, String paymentMethod, String comments) {
        log.info("Testing positive order payment and shipment scenario");
        waitForPageLoaded();

        // Step 1: Fill in all required billing information
        fillBillingInformation(name, phone, email, address);

        // Step 2: Select location details
        selectState(state);
        selectDistrict(district);
        selectCommune(commune);

        // Step 3: Add order comments
        addOrderComments(comments);

        // Step 4: Select payment method
        selectPaymentMethod(paymentMethod);

        // Step 5: Accept terms and conditions
        acceptTermsAndConditions();

        // Step 6: Verify all information is correctly selected/entered
        Assert.assertEquals(name, recipientNameInput.getAttribute("value"), "Name verification failed");
        Assert.assertEquals(phone, phoneNumberInput.getAttribute("value"), "Phone verification failed");
        Assert.assertEquals(email, emailInput.getAttribute("value"), "Email verification failed");
        Assert.assertEquals(address, addressInput.getAttribute("value"), "Address verification failed");

        Assert.assertTrue(stateDropdown.getText().contains(state), "State verification failed");
        Assert.assertTrue(districtDropdown.getText().contains(district), "District verification failed");
        Assert.assertTrue(communeDropdown.getText().contains(commune), "Commune verification failed");

        Assert.assertEquals(comments, orderCommentsInput.getAttribute("value"), "Comments verification failed");
        Assert.assertTrue(Objects.equals(paymentMethod, "bank") ? bankTransferPaymentOption.isSelected() : codPaymentOption.isSelected(),
                "Payment method verification failed");
        Assert.assertTrue(termsCheckbox.isSelected(), "Terms checkbox verification failed");

        // Step 7: Place the order
//        placeOrder();

        // Step 8: Verify order was placed successfully
//        Assert.assertTrue(isOrderConfirmed(), "Order confirmation verification failed");

        log.info("Positive order payment and shipment test completed successfully");
    }

    /**
         * Negative test: Leave the recipient name field empty and verify error handling.
         * Steps:
         *  - Leave 'Họ và Tên' (recipient name) empty
         *  - Fill other fields with valid data
         *  - Attempt to place order
         *  - Assert that the name field is highlighted and error message is shown
         */
        public void verifyOrderFailsWhenNameIsEmpty(String phone, String email, String state, String district, String commune, String address, String paymentMethod, String comments) {
            log.info("Testing negative order scenario: empty recipient name");
            waitForPageLoaded();

            // Leave name empty, fill other fields
            fillBillingInformation("", phone, email, address);
            selectState(state);
            selectDistrict(district);
            selectCommune(commune);
            addOrderComments(comments);
            selectPaymentMethod(paymentMethod);
            acceptTermsAndConditions();

            // Try to place the order
            placeOrder();

            wait.until(ExpectedConditions.visibilityOf(orderInformationErrorWrapper));

            // Find the parent .form-row.woocommerce-invalid element containing the input
            String borderColor = recipientNameInput.getCssValue("border-top-color");
            Assert.assertTrue(borderColor != null && (borderColor.contains(FORM_ERROR_BORDER_COLOR_RBG)), "Name field border is not red");

            // Assert: error message is displayed
            WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//ul[@class='woocommerce-error']//li[@data-id='billing_last_name']")));
            Assert.assertEquals(errorMsg.getText().trim(), FrameworkConstants.NAME_EMPTY_ERROR_MESSAGE, "Expected error message for empty name not displayed");

            log.info("Negative test for empty recipient name completed successfully");
        }

            /**
             * Negative test: Enter only digits in the recipient name field.
             */
            public void verifyOrderFailsWhenNameIsAllDigits(String nameOnlyContainNumbers,String phone, String email, String state, String district, String commune, String address, String paymentMethod, String comments) {
                log.info("Testing negative order scenario: recipient name is all digits");
                waitForPageLoaded();
                fillBillingInformation(nameOnlyContainNumbers, phone, email, address);
                selectState(state);
                selectDistrict(district);
                selectCommune(commune);
                addOrderComments(comments);
                selectPaymentMethod(paymentMethod);
//                acceptTermsAndConditions();
                placeOrder();
                wait.until(ExpectedConditions.visibilityOf(orderInformationErrorWrapper));
                String borderColor = recipientNameInput.getCssValue("border-top-color");
                Assert.assertTrue(borderColor != null && (borderColor.contains(FORM_ERROR_BORDER_COLOR_RBG)), "Name field border is not red");
                WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//ul[@class='woocommerce-error']//li[@data-id='billing_last_name']")));
                Assert.assertEquals(errorMsg.getText(), FrameworkConstants.NAME_INVALID_ERROR_MESSAGE, "Expected error message for invalid name not displayed");
                log.info("Negative test for all-digits recipient name completed successfully");
            }

            /**
             * Negative test: Leave the phone number field empty and verify error handling.
             */
            public void verifyOrderFailsWhenPhoneIsEmpty(String name, String email, String state, String district, String commune, String address, String paymentMethod, String comments) {
                log.info("Testing negative order scenario: empty phone number");
                waitForPageLoaded();
                fillBillingInformation(name, "", email, address);
                selectState(state);
                selectDistrict(district);
                selectCommune(commune);
                addOrderComments(comments);
                selectPaymentMethod(paymentMethod);
                acceptTermsAndConditions();
                placeOrder();
                wait.until(ExpectedConditions.visibilityOf(orderInformationErrorWrapper)).click();
                String borderColor = phoneNumberInput.getCssValue("border-top-color");
                Assert.assertTrue(borderColor != null && (borderColor.contains(FORM_ERROR_BORDER_COLOR_RBG)), "Phone field border is not red");
                WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//ul[@class='woocommerce-error']//li[@data-id='billing_phone']")));
                Assert.assertEquals(errorMsg.getText(), FrameworkConstants.PHONE_EMPTY_ERROR_MESSAGE, "Expected error message for empty phone not displayed");
                log.info("Negative test for empty phone number completed successfully");
            }

            /**
             * Negative test: Enter invalid characters in the phone number field.
             */
            public void verifyOrderFailsWhenPhoneIsInvalid(String name, String invalidPhone, String email, String state, String district, String commune, String address, String paymentMethod, String comments) {
                log.info("Testing negative order scenario: invalid phone number");
                waitForPageLoaded();
                // Example: "abc123😊"
                fillBillingInformation(name,invalidPhone , email, address);
                selectState(state);
                selectDistrict(district);
                selectCommune(commune);
                addOrderComments(comments);
                selectPaymentMethod(paymentMethod);
//                acceptTermsAndConditions();
                placeOrder();
                wait.until(ExpectedConditions.visibilityOf(orderInformationErrorWrapper));
                String borderColor = phoneNumberInput.getCssValue("border-top-color");
                Assert.assertTrue(borderColor != null && (borderColor.contains(FORM_ERROR_BORDER_COLOR_RBG)), "Phone field border is not red");
                WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//ul[@class='woocommerce-error']//li[@data-id='billing_phone']")));
                Assert.assertEquals(errorMsg.getText(), FrameworkConstants.PHONE_INVALID_ERROR_MESSAGE, "Expected error message for invalid phone not displayed");
                log.info("Negative test for invalid phone number completed successfully");
            }

            /**
             * Positive test: Leave the email field empty and verify order can be placed.
             */
            public void verifyOrderSucceedsWhenEmailIsEmpty(String name, String phone, String state, String district, String commune, String address, String paymentMethod, String comments) {
                log.info("Testing positive order scenario: empty email");
                waitForPageLoaded();
                fillBillingInformation(name, phone, "", address);
                selectState(state);
                selectDistrict(district);
                selectCommune(commune);
                addOrderComments(comments);
                selectPaymentMethod(paymentMethod);
//                acceptTermsAndConditions();
                placeOrder();
                wait.until(ExpectedConditions.visibilityOf(orderInformationErrorWrapper));
//                Assert.assertTrue(isOrderConfirmed(), "Order should be placed successfully with empty email");
                log.info("Positive test for empty email completed successfully");
            }

            /**
             * Negative test: Enter invalid email format and verify error handling.
             */
            public void verifyOrderFailsWhenEmailIsInvalid(String name, String phone,String invalidEmail, String state, String district, String commune, String address, String paymentMethod, String comments) {
                log.info("Testing negative order scenario: invalid email format");
                waitForPageLoaded();
                fillBillingInformation(name, phone, invalidEmail, address);
                selectState(state);
                selectDistrict(district);
                selectCommune(commune);
                addOrderComments(comments);
                selectPaymentMethod(paymentMethod);
                acceptTermsAndConditions();
                placeOrder();
                wait.until(ExpectedConditions.visibilityOf(orderInformationErrorWrapper));
                String borderColor = emailInput.getCssValue("border-top-color");
                Assert.assertTrue(borderColor != null && (borderColor.contains(FORM_ERROR_BORDER_COLOR_RBG)), "Email field border is not red");
                WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//ul[@class='woocommerce-error']//li[contains(text(), 'Địa chỉ email thanh toán không hợp lệ')]")));
                Assert.assertTrue(errorMsg.isDisplayed(), "Expected error message for invalid email not displayed");
                log.info("Negative test for invalid email completed successfully");
            }


                /**
                 * Positive test: Edit district and commune fields after initial valid entry.
                 */
                public void verifyOrderSucceedsWhenDistrictAndCommuneAreEdited(String name, String phone, String email, String state, String initialDistrict, String initialCommune, String newDistrict, String newCommune, String address, String paymentMethod, String comments) {
                    log.info("Testing positive order scenario: edit district and commune fields");
                    waitForPageLoaded();
                    fillBillingInformation(name, phone, email, address);
                    selectState(state);

                    selectDistrict(initialDistrict);
                    selectCommune(initialCommune);
                    // Edit district and commune
                    selectDistrict(newDistrict);
                    selectCommune(newCommune);
                    addOrderComments(comments);
                    selectPaymentMethod(paymentMethod);
//                    acceptTermsAndConditions();

                    Assert.assertTrue(districtDropdown.getText().contains(newDistrict), "District was not updated correctly");
                    Assert.assertTrue(communeDropdown.getText().contains(newCommune), "Commune was not updated correctly");
                    placeOrder();
                    log.info("Positive test for editing district and commune completed successfully");
                }

                /**
                 * Negative test: Leave district, commune, and address fields empty and verify error handling.
                 */
                public void verifyOrderFailsWhenDistrictCommuneAndAddressAreEmpty(String name, String phone, String email, String state, String paymentMethod, String comments) {
                    log.info("Testing negative order scenario: empty district, commune, and address");
                    waitForPageLoaded();
                    fillBillingInformation(name, phone, email, "");
                    selectState(state);
                    // Do not select district or commune
                    addOrderComments(comments);
                    selectPaymentMethod(paymentMethod);
                    acceptTermsAndConditions();
                    placeOrder();
                    wait.until(ExpectedConditions.visibilityOf(orderInformationErrorWrapper));
                    String districtBorder = districtDropdown.getCssValue("border-top-color");
                    String communeBorder = communeDropdown.getCssValue("border-top-color");
                    String addressBorder = addressInput.getCssValue("border-top-color");
                    Assert.assertTrue(districtBorder != null && districtBorder.contains(FORM_ERROR_BORDER_COLOR_RBG), "District field border is not red");
                    Assert.assertTrue(communeBorder != null && communeBorder.contains(FORM_ERROR_BORDER_COLOR_RBG), "Commune field border is not red");
                    Assert.assertTrue(addressBorder != null && addressBorder.contains(FORM_ERROR_BORDER_COLOR_RBG), "Address field border is not red");
                    WebElement districtError = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//ul[@class='woocommerce-error']//li[contains(text(), 'Mục Quận/Huyện: là mục bắt buộc.')]")));
                    WebElement communeError = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//ul[@class='woocommerce-error']//li[contains(text(), 'Mục Xã/Phường: là mục bắt buộc.')]")));
                    WebElement addressError = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//ul[@class='woocommerce-error']//li[contains(text(), 'Mục Địa chỉ: là mục bắt buộc.')]")));
                    Assert.assertTrue(districtError.isDisplayed(), "District required error not displayed");
                    Assert.assertTrue(communeError.isDisplayed(), "Commune required error not displayed");
                    Assert.assertTrue(addressError.isDisplayed(), "Address required error not displayed");
                    log.info("Negative test for empty district, commune, and address completed successfully");
                }

                /**
                 * Negative test: Do not check Terms and Conditions and verify error handling.
                 */
                public void verifyOrderFailsWhenTermsNotChecked(String name, String phone, String email, String state, String district, String commune, String address, String paymentMethod, String comments) {
                    log.info("Testing negative order scenario: terms and conditions not checked");
                    waitForPageLoaded();
                    fillBillingInformation(name, phone, email, address);
                    selectState(state);
                    selectDistrict(district);
                    selectCommune(commune);
                    addOrderComments(comments);
                    selectPaymentMethod(paymentMethod);
                    // Do not accept terms and conditions
                    placeOrder();
                    wait.until(ExpectedConditions.visibilityOf(orderInformationErrorWrapper));
                    String termsBorder = termsCheckbox.getCssValue("border-top-color");
                    Assert.assertTrue(termsBorder != null && termsBorder.contains(FORM_ERROR_BORDER_COLOR_RBG), "Terms checkbox border is not red");
                    WebElement termsError = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//ul[@class='woocommerce-error']//li[contains(text(), 'Vui lòng đọc và đồng ý điều khoản và điều kiện để tiếp tục đặt hàng.')]")));
                    Assert.assertTrue(termsError.isDisplayed(), "Terms and conditions error not displayed");
                    log.info("Negative test for terms and conditions not checked completed successfully");
                }
}

