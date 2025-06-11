package pages;

import constants.FrameworkConstants;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;
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
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object Model for the View Cart Page.
 * Handles all interactions with the shopping cart page.
 */
public class ViewCartPage {
    private static final Logger log = LoggerFactory.getLogger(ViewCartPage.class);
    private static final String CART_URL = "https://theblues.com.vn/cart/";
    private static final long DEFAULT_TIMEOUT = 15;
    private static final long SHORT_TIMEOUT = 5;

    private final WebDriver webDriver;
    private final WebDriverWait wait;

    // Cart elements
    @FindBy(css = "input.qty")
    private WebElement quantityInputField;

    @FindBy(css = ".checkout-button")
    private WebElement checkoutButton;

    // Shipping calculator elements
    @FindBy(css = ".shipping-calculator-button")
    private WebElement addressModificationButton;

    @FindBy(css = "button[name='calc_shipping']")
    private WebElement updateAddressButton;

    @FindBy(css = ".woocommerce-notices-wrapper")
    private WebElement noticeUpdateAddressWrapper;

    // Country dropdown elements
    @FindBy(id = "calc_shipping_country")
    private WebElement countryDropdown;

    @FindBy(id = "select2-calc_shipping_country-container")
    private WebElement countryDropdownButton;

    @FindBy(id = "select2-calc_shipping_country-results")
    private WebElement countryResultsList;

    // State dropdown elements
    @FindBy(id = "calc_shipping_state")
    private WebElement stateDropdown;

    @FindBy(id = "select2-calc_shipping_state-container")
    private WebElement stateDropdownButton;

    @FindBy(id = "select2-calc_shipping_state-results")
    private WebElement stateResultsList;

    // City dropdown elements
    @FindBy(id = "calc_shipping_city")
    private WebElement cityDropdown;

    @FindBy(id = "select2-calc_shipping_city-container")
    private WebElement cityDropdownButton;

    @FindBy(id = "select2-calc_shipping_city-results")
    private WebElement cityResultsList;

    // Common dropdown search field
    @FindBy(css = ".select2-search__field")
    private WebElement addressSearchInput;

    @FindBy(css = ".woocommerce-shipping-destination")
    private WebElement shippingDestinationSpan;

    @FindBy(className = "woocommerce-cart-form__contents")
    private WebElement cartItemsTable;

    private WebElement deleteCartItemButton;

    private WebElement itemQuantityInput;

    @FindBy(className = "restore-item")
    private WebElement restoreItemButton;
    @FindBy(className = "woocommerce-notices-wrapper")
    private WebElement noticeWrapper;

    @FindBy(className = "blockOverlay")
    private WebElement blockOverlay;

    @FindBy(className = "wc-backward")
    private WebElement backToShopButton;

    @FindBy(className = "cart-empty")
    private WebElement emptyCartMessage;
    /**
     * Constructor initializes the ViewCartPage with WebDriver
     * @param webDriver The WebDriver instance
     */
    public ViewCartPage(WebDriver webDriver) {
        this.webDriver = webDriver;
//        webDriver.get(CART_URL);
        PageFactory.initElements(webDriver, this);
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public String getNoticeText() {
        log.info("Fetching notice text from the cart page");
        wait.until(ExpectedConditions.visibilityOf(noticeWrapper));
        List<WebElement> noticeElements = noticeWrapper.findElements(By.className("woocommerce-message"));
        String noticeText=  noticeElements.get(0).getText();
        log.debug("Notice text: {}", noticeText);
        return noticeText;
    }

    public void deleteCartItem(int itemIndex) {
        log.info("Deleting cart item at index: {}", itemIndex);
        List<WebElement> itemsTableElements = cartItemsTable.findElements(By.tagName("tr"));
        if (itemsTableElements.isEmpty()) {
            log.warn("No items found in the cart to delete.");
            return;
        }
        if (itemIndex < 0 || itemIndex >= itemsTableElements.size()) {
            log.warn("Invalid item index: {}. Available items: {}", itemIndex, itemsTableElements.size());
            return;
        }
        WebElement targetDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(itemsTableElements.get(itemIndex).findElement(By.cssSelector(".remove"))));
        targetDeleteButton.click();
        wait.until(ExpectedConditions.visibilityOf(restoreItemButton));
        log.info("Cart item at index {} deleted successfully", itemIndex);
        Allure.step("Delete cart item at index: " + itemIndex);
    }

    public void setQuantityInput(int index, int quantity) {
        log.info("Setting quantity input for item at index {} to {}", index, quantity);
        List<WebElement> itemsTableElements = cartItemsTable.findElements(By.tagName("tr"));
        if (itemsTableElements.isEmpty()) {
            log.warn("No items found in the cart to set quantity.");
            return;
        }
        if (index < 0 || index >= itemsTableElements.size()) {
            log.warn("Invalid item index: {}. Available items: {}", index, itemsTableElements.size());
            return;
        }
        itemQuantityInput = itemsTableElements.get(index).findElement(By.tagName("input"));
        itemQuantityInput.clear();
        itemQuantityInput.sendKeys(String.valueOf(quantity));
        log.info("Quantity input for item at index {} set to {}", index, quantity);
    }

    /**
     * Modifies the shipping address with the provided location details
     * This method is kept as per original signature to maintain compatibility
     * @param addressLine1 The country name
     * @param addressLine2 The state/province name
     * @param addressLine3 The city name
     */
    public void modifyShippingAddress(String addressLine1, String addressLine2, String addressLine3) {
        log.info("Modifying shipping address with country: {}, state: {}, city: {}",
                addressLine1, addressLine2, addressLine3);

        // Wait for elements and scroll to address section
        Actions actions = new Actions(webDriver);
        wait.until(ExpectedConditions.invisibilityOf(blockOverlay));

        // Move to checkout button to ensure address section is visible
        actions.moveToElement(checkoutButton).perform();

        // Click on address modification button
        addressModificationButton.click();

        // Select country
        selectAddressOption(countryDropdownButton, countryResultsList, addressLine1);


        // Select state
        selectAddressOption(stateDropdownButton, stateResultsList, addressLine2);

        // Wait for city dropdown to become interactive after state selection
        waitForElementToBeInteractive(cityDropdownButton);

        // Select city
        selectAddressOption(cityDropdownButton, cityResultsList, addressLine3);

        // Update the address
        wait.until(ExpectedConditions.elementToBeClickable(updateAddressButton)).click();

        // Verify the notice appears, indicating successful update
        wait.until(ExpectedConditions.visibilityOf(noticeUpdateAddressWrapper));
        log.debug(shippingDestinationSpan.getText());

        Assert.assertTrue(shippingDestinationSpan.getText().contains(addressLine2));
        Assert.assertTrue(shippingDestinationSpan.getText().contains(addressLine3));

        log.info("Shipping address updated successfully");
    }

    /**
     * Selects an address option from a dropdown
     * @param dropdownButton The dropdown button element
     * @param resultsList The results list element
     * @param optionText The text of the option to select
     */
    private void selectAddressOption(WebElement dropdownButton, WebElement resultsList, String optionText) {
        // Open dropdown
        wait.until(ExpectedConditions.elementToBeClickable(dropdownButton)).click();

        // Enter search text if input field is available
        try {
            WebElement searchField = wait.until(ExpectedConditions.visibilityOf(addressSearchInput));
            searchField.clear();
            searchField.sendKeys(optionText);
        } catch (Exception e) {
            log.debug("Search input not found or not needed for {}", optionText);
        }

        // Wait for results to appear
        wait.until(ExpectedConditions.visibilityOf(resultsList));

        // Find and click the matching option
        List<WebElement> matchingOptions = resultsList.findElements(By.tagName("li"))
                .stream()
                .filter(option -> option.getText().contains(optionText))
                .collect(Collectors.toList());

        if (!matchingOptions.isEmpty()) {
            WebElement targetOption = matchingOptions.get(0);
            wait.until(ExpectedConditions.elementToBeClickable(targetOption)).click();
            log.debug("Selected option: {}", optionText);
        } else {
            log.warn("No address option found matching: {}", optionText);
        }

        // Wait for dropdown to close and show selected value
        wait.until(ExpectedConditions.visibilityOf(dropdownButton));
        log.debug("Selected value: {}", dropdownButton.getText());

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(dropdownButton.getText().contains(optionText));
    }

    /**
     * Waits for an element to become interactive after a state change
     * @param element The WebElement to check
     */
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
        log.info("Cart page is fully loaded.");
    }



    /**
     * Updates the quantity of an item in the cart
     * @param quantity The new quantity
     */
    public void updateItemQuantity(int quantity) {
        wait.until(ExpectedConditions.visibilityOf(quantityInputField)).clear();
        quantityInputField.sendKeys(String.valueOf(quantity));
        log.info("Updated item quantity to {}", quantity);
    }

    /**
     * Navigates to the order shipping and payment page
     * This method is kept as per original signature to maintain compatibility
     * @return An instance of OrderShippingAndPaymentPage
     */
    public OrderShippingAndPaymentPage navigateToOrderShippingAndPaymentPage() {
        log.info("Navigating to order shipping and payment page");
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        return new OrderShippingAndPaymentPage(webDriver);
    }

    /**
     * Alternative method to select country using the Select class
     * @param countryValue The country value to select
     */
    public void selectCountry(String countryValue) {
        log.info("Selecting country: {}", countryValue);
        try {
            Select countrySelect = new Select(wait.until(ExpectedConditions.visibilityOf(countryDropdown)));
            countrySelect.selectByValue(countryValue);
        } catch (Exception e) {
            log.debug("Regular Select doesn't work, using custom dropdown handling");
            selectAddressOption(countryDropdownButton, countryResultsList, countryValue);
        }
    }

    /**
     * Alternative method to select state using the Select class
     * @param stateValue The state value to select
     */
    public void selectState(String stateValue) {
        log.info("Selecting state/province: {}", stateValue);
        try {
            Select stateSelect = new Select(wait.until(ExpectedConditions.visibilityOf(stateDropdown)));
            stateSelect.selectByValue(stateValue);
        } catch (Exception e) {
            log.debug("Regular Select doesn't work, using custom dropdown handling");
            selectAddressOption(stateDropdownButton, stateResultsList, stateValue);
        }
    }

    /**
     * Alternative method to select city using the Select class
     * @param cityValue The city value to select
     */
    public void selectCity(String cityValue) {
        log.info("Selecting city: {}", cityValue);
        try {
            Select citySelect = new Select(wait.until(ExpectedConditions.visibilityOf(cityDropdown)));
            citySelect.selectByValue(cityValue);
        } catch (Exception e) {
            log.debug("Regular Select doesn't work, using custom dropdown handling");
            selectAddressOption(cityDropdownButton, cityResultsList, cityValue);
        }
    }

    /**
     * Modern version of modifyShippingAddress with better parameter names
     * @param country The country name
     * @param state The state/province name
     * @param city The city name
     */
    public void updateShippingAddress(String country, String state, String city) {
        log.info("Updating shipping address with country: {}, state: {}, city: {}",
                country, state, city);

        // Open the shipping calculator
        Actions actions = new Actions(webDriver);
        wait.until(ExpectedConditions.visibilityOf(addressModificationButton));
        wait.until(ExpectedConditions.visibilityOf(checkoutButton));
        actions.moveToElement(checkoutButton).perform();
        addressModificationButton.click();

        // Select address details using Select class when possible
        selectCountry(country);
        waitForElementToBeInteractive(stateDropdown);
        selectState(state);
        waitForElementToBeInteractive(cityDropdown);
        selectCity(city);

        // Submit and verify
        wait.until(ExpectedConditions.elementToBeClickable(updateAddressButton)).click();
        wait.until(ExpectedConditions.visibilityOf(noticeUpdateAddressWrapper));
        log.info("Shipping address updated successfully");
    }

    public void deleteAndRestoreCartItem(int itemIndex) {
        waitForPageLoaded();
        log.info("Deleting and restoring cart item at index: {}", itemIndex);
        deleteCartItem(itemIndex);
        wait.until(ExpectedConditions.visibilityOf(noticeWrapper.findElement(By.className("woocommerce-message"))));
        log.info("Cart item at index {} deleted successfully", itemIndex);
        Assert.assertTrue(getNoticeText().contains("đã xóa"), "Item was not delete successfully");
        wait.until(ExpectedConditions.elementToBeClickable(restoreItemButton)).click();

        log.info("Cart item at index {} restored successfully", itemIndex);
        Allure.step("Delete and restore cart item at index: " + itemIndex);
    }

    public void setItemQuantityToZeroToDelete(int itemIndex) {
        waitForPageLoaded();
        log.info("Setting item quantity to zero for deletion at index: {}", itemIndex);
        setQuantityInput(itemIndex, 0);
        log.info("Item quantity set to zero and cart updated");
        wait.until(ExpectedConditions.visibilityOf(noticeWrapper));
        String noticeText = getNoticeText();
        Assert.assertEquals(noticeText, "Giỏ hàng đã được cập nhật.", "Item was not deleted successfully after setting quantity to zero");
    }

    public void deleteAllItemsFromCart() {
        waitForPageLoaded();
        log.info("Deleting all items from the cart");
        List<WebElement> itemsTableElements = cartItemsTable.findElements(By.tagName("tr"));
        if (itemsTableElements.isEmpty()) {
            log.warn("No items found in the cart to delete.");
            return;
        }
        for(int i = 1; i < itemsTableElements.size(); i++) {
            log.info("Deleting items numbered: {}", i);
            deleteCartItem(1);
            wait.until(ExpectedConditions.visibilityOf(noticeWrapper));
            String noticeText = getNoticeText();
            Assert.assertTrue(noticeText.contains("đã xóa"), "Item was not deleted successfully");
        }
        log.info("All items deleted from the cart");

        Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(checkoutButton)));
        Assert.assertEquals(wait.until(ExpectedConditions.visibilityOf(emptyCartMessage)).getText(), FrameworkConstants.CART_EMPTY_MESSAGE);
        Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(backToShopButton)).isEnabled());
    }
}
