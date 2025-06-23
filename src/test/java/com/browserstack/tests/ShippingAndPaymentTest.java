package com.browserstack.tests;

import com.browserstack.common.ExcelHelpers;
import constants.FrameworkConstants;
import constants.TestExcelDataUtils;
import io.qameta.allure.Description;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.OrderShippingAndPaymentPage;
import utils.DriverManager;

import static constants.TestExcelDataUtils.*;
import static utils.DriverManager.clearBrowserData;

public class ShippingAndPaymentTest extends SeleniumTest{

    ExcelHelpers shippingAndPaymentData = new ExcelHelpers();


    @BeforeMethod(alwaysRun = true, firstTimeOnly = true
    )
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        driver = DriverManager.getInstance().getDriver();
        clearBrowserData();
        ChooseItemVariations.addProductToCartWithVariations();
    }

    @AfterMethod(alwaysRun = true, lastTimeOnly = true, onlyForGroups = {"ClearCookies"})
    public void clearCookies() throws Exception {
        Thread.sleep(5000);
        clearBrowserData();
    }

    @AfterMethod(alwaysRun = true, lastTimeOnly = true
//            , onlyForGroups = {"QuitDriver"}
    )
    public void tearDown() throws Exception {
        driver = DriverManager.getInstance().getDriver();
        driver.quit();
    }


    @Test(dependsOnMethods = {
//              "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations",
//           "com.browserstack.tests.ViewCartTest.setShipingAddress"
   }, groups = {"ClearCookies"}, skipFailedInvocations = true)
    public void createOrderWithAllValidData() throws Exception {
       OrderShippingAndPaymentPage orderShippingAndPaymentPage = new OrderShippingAndPaymentPage(driver);
       driver.get(FrameworkConstants.SHIPMENT_AND_PAYMENT_URL);
       shippingAndPaymentData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH,TestExcelDataUtils.SHIPMENT_PAYMENT_DATA_SHEET);
       int recordNumber = 1; // Record number in the Excel file to be used for this test
       orderShippingAndPaymentPage.verifyOrderPaymentAndShipment(
               shippingAndPaymentData.getCellData(NAME,recordNumber),
               shippingAndPaymentData.getCellData(PHONE,recordNumber),
               shippingAndPaymentData.getCellData(EMAIL,recordNumber),
               shippingAndPaymentData.getCellData(CITY,recordNumber),
               shippingAndPaymentData.getCellData(DISTRICT,recordNumber),
               shippingAndPaymentData.getCellData(COMMUNE,recordNumber),
               shippingAndPaymentData.getCellData(DETAIL_ADDRESS,recordNumber),
               shippingAndPaymentData.getCellData(PAYMENT_METHOD,recordNumber),
               shippingAndPaymentData.getCellData(COMMENT,recordNumber)
       );

    }

    @Test(
            testName = "TC_SP_2"
//            ,dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations",
//            groups = {"ShippingAndPaymentTest"}
    )
    @Description("Create order with empty name field")
    public void createOrderWithEmptyName() throws Exception {
        OrderShippingAndPaymentPage orderShippingAndPaymentPage = new OrderShippingAndPaymentPage(driver);
        driver.get(FrameworkConstants.SHIPMENT_AND_PAYMENT_URL);
        shippingAndPaymentData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH,TestExcelDataUtils.SHIPMENT_PAYMENT_DATA_SHEET);
        int recordNumber = 1; // Record number in the Excel file to be used for this test
        orderShippingAndPaymentPage.verifyOrderFailsWhenNameIsEmpty(
                shippingAndPaymentData.getCellData(PHONE,recordNumber),
                shippingAndPaymentData.getCellData(EMAIL,recordNumber),
                shippingAndPaymentData.getCellData(CITY,recordNumber),
                shippingAndPaymentData.getCellData(DISTRICT,recordNumber),
                shippingAndPaymentData.getCellData(COMMUNE,recordNumber),
                shippingAndPaymentData.getCellData(DETAIL_ADDRESS,recordNumber),
                shippingAndPaymentData.getCellData(PAYMENT_METHOD,recordNumber),
                shippingAndPaymentData.getCellData(COMMENT,recordNumber)
        );
    }

    @Test(
            testName = "TC_SP_3"
//            dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations"
    )
    @Description("Create order with invalid name field")
    public void verifyOrderFailsWhenNameIsAllDigits() throws Exception {
        OrderShippingAndPaymentPage orderShippingAndPaymentPage = new OrderShippingAndPaymentPage(driver);
        driver.get(FrameworkConstants.SHIPMENT_AND_PAYMENT_URL);
        shippingAndPaymentData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH,TestExcelDataUtils.SHIPMENT_PAYMENT_DATA_SHEET);
        int recordNumber = 5; // Record number in the Excel file to be used for this test
        orderShippingAndPaymentPage.verifyOrderFailsWhenNameIsAllDigits(
                shippingAndPaymentData.getCellData(NAME,recordNumber),
                shippingAndPaymentData.getCellData(PHONE,recordNumber),
                shippingAndPaymentData.getCellData(EMAIL,recordNumber),
                shippingAndPaymentData.getCellData(CITY,recordNumber),
                shippingAndPaymentData.getCellData(DISTRICT,recordNumber),
                shippingAndPaymentData.getCellData(COMMUNE,recordNumber),
                shippingAndPaymentData.getCellData(DETAIL_ADDRESS,recordNumber),
                shippingAndPaymentData.getCellData(PAYMENT_METHOD,recordNumber),
                shippingAndPaymentData.getCellData(COMMENT,recordNumber)
        );
    }

    @Test(
            testName = "TC_SP_4"
//            dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations"
    )
    @Description("Create order with empty phone field")
    public void verifyOrderFailsWhenPhoneIsEmpty() throws Exception {
        OrderShippingAndPaymentPage orderShippingAndPaymentPage = new OrderShippingAndPaymentPage(driver);
        driver.get(FrameworkConstants.SHIPMENT_AND_PAYMENT_URL);
        shippingAndPaymentData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH,TestExcelDataUtils.SHIPMENT_PAYMENT_DATA_SHEET);
        int recordNumber = 6; // Record number in the Excel file to be used for this test
        orderShippingAndPaymentPage.verifyOrderFailsWhenPhoneIsEmpty(
                shippingAndPaymentData.getCellData(NAME,recordNumber),
                shippingAndPaymentData.getCellData(EMAIL,recordNumber),
                shippingAndPaymentData.getCellData(CITY,recordNumber),
                shippingAndPaymentData.getCellData(DISTRICT,recordNumber),
                shippingAndPaymentData.getCellData(COMMUNE,recordNumber),
                shippingAndPaymentData.getCellData(DETAIL_ADDRESS,recordNumber),
                shippingAndPaymentData.getCellData(PAYMENT_METHOD,recordNumber),
                shippingAndPaymentData.getCellData(COMMENT,recordNumber)
        );
    }

    @Test(
            testName = "TC_SP_5"
//            dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations"
    )
    @Description("Create order with invalid phone field")
    public void verifyOrderFailsWhenPhoneIsInvalid() throws Exception {
        OrderShippingAndPaymentPage orderShippingAndPaymentPage = new OrderShippingAndPaymentPage(driver);
        driver.get(FrameworkConstants.SHIPMENT_AND_PAYMENT_URL);
        shippingAndPaymentData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH,TestExcelDataUtils.SHIPMENT_PAYMENT_DATA_SHEET);
        int recordNumber = 7; // Record number in the Excel file to be used for this test
        orderShippingAndPaymentPage.verifyOrderFailsWhenPhoneIsInvalid(
                shippingAndPaymentData.getCellData(NAME,recordNumber),
                shippingAndPaymentData.getCellData(PHONE,recordNumber),
                shippingAndPaymentData.getCellData(EMAIL,recordNumber),
                shippingAndPaymentData.getCellData(CITY,recordNumber),
                shippingAndPaymentData.getCellData(DISTRICT,recordNumber),
                shippingAndPaymentData.getCellData(COMMUNE,recordNumber),
                shippingAndPaymentData.getCellData(DETAIL_ADDRESS,recordNumber),
                shippingAndPaymentData.getCellData(PAYMENT_METHOD,recordNumber),
                shippingAndPaymentData.getCellData(COMMENT,recordNumber)
        );
    }

    @Test(
            testName = "TC_SP_6"
//            dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations"
    )
    @Description("Create order with empty email field")
    public void verifyOrderSucceedsWhenEmailIsEmpty() throws Exception {
        OrderShippingAndPaymentPage orderShippingAndPaymentPage = new OrderShippingAndPaymentPage(driver);
        driver.get(FrameworkConstants.SHIPMENT_AND_PAYMENT_URL);
        shippingAndPaymentData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH,TestExcelDataUtils.SHIPMENT_PAYMENT_DATA_SHEET);
        int recordNumber = 8; // Record number in the Excel file to be used for this test
        orderShippingAndPaymentPage.verifyOrderSucceedsWhenEmailIsEmpty(
                shippingAndPaymentData.getCellData(NAME,recordNumber),
                shippingAndPaymentData.getCellData(PHONE,recordNumber),
                shippingAndPaymentData.getCellData(CITY,recordNumber),
                shippingAndPaymentData.getCellData(DISTRICT,recordNumber),
                shippingAndPaymentData.getCellData(COMMUNE,recordNumber),
                shippingAndPaymentData.getCellData(DETAIL_ADDRESS,recordNumber),
                shippingAndPaymentData.getCellData(PAYMENT_METHOD,recordNumber),
                shippingAndPaymentData.getCellData(COMMENT,recordNumber)
        );
    }

    @Test(
            testName = "TC_SP_7"
//            dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations"
    )
    @Description("Create order with invalid email field")
    public void verifyOrderFailsWhenEmailIsInvalid() throws Exception {
        OrderShippingAndPaymentPage orderShippingAndPaymentPage = new OrderShippingAndPaymentPage(driver);
        driver.get(FrameworkConstants.SHIPMENT_AND_PAYMENT_URL);
        shippingAndPaymentData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH,TestExcelDataUtils.SHIPMENT_PAYMENT_DATA_SHEET);
        int recordNumber = 9; // Record number in the Excel file to be used for this test
        orderShippingAndPaymentPage.verifyOrderFailsWhenEmailIsInvalid(
                shippingAndPaymentData.getCellData(NAME,recordNumber),
                shippingAndPaymentData.getCellData(PHONE,recordNumber),
                shippingAndPaymentData.getCellData(EMAIL,recordNumber),
                shippingAndPaymentData.getCellData(CITY,recordNumber),
                shippingAndPaymentData.getCellData(DISTRICT,recordNumber),
                shippingAndPaymentData.getCellData(COMMUNE,recordNumber),
                shippingAndPaymentData.getCellData(DETAIL_ADDRESS,recordNumber),
                shippingAndPaymentData.getCellData(PAYMENT_METHOD,recordNumber),
                shippingAndPaymentData.getCellData(COMMENT,recordNumber)
        );
    }

    @Test(
            testName = "TC_SP_8"
//            dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations", groups = {"ClearCookies"}
    )
    @Description("Create order with modified district and commune fields")
    public void verifyOrderSucceedsWhenDistrictAndCommuneAreEdited() throws Exception {
        OrderShippingAndPaymentPage orderShippingAndPaymentPage = new OrderShippingAndPaymentPage(driver);
        driver.get(FrameworkConstants.SHIPMENT_AND_PAYMENT_URL);
        shippingAndPaymentData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH,TestExcelDataUtils.SHIPMENT_PAYMENT_DATA_SHEET);
        int recordNumber = 10; // Record number in the Excel file to be used for this test
        orderShippingAndPaymentPage.verifyOrderSucceedsWhenDistrictAndCommuneAreEdited(
                shippingAndPaymentData.getCellData(NAME,recordNumber),
                shippingAndPaymentData.getCellData(PHONE,recordNumber),
                shippingAndPaymentData.getCellData(EMAIL,recordNumber),
                shippingAndPaymentData.getCellData(CITY,recordNumber),
                shippingAndPaymentData.getCellData(DISTRICT,recordNumber),
                shippingAndPaymentData.getCellData(COMMUNE,recordNumber),
                shippingAndPaymentData.getCellData(EDIT_DISTRICT,recordNumber),
                shippingAndPaymentData.getCellData(EDIT_COMMUNE,recordNumber),
                shippingAndPaymentData.getCellData(DETAIL_ADDRESS,recordNumber),
                shippingAndPaymentData.getCellData(PAYMENT_METHOD,recordNumber),
                shippingAndPaymentData.getCellData(COMMENT,recordNumber)
        );
    }

    @Test(
            testName = "TC_SP_9"
//            dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations"
    )
    @Description("Create order with all address fields empty")
    public void verifyOrderFailsWhenDistrictCommuneAndAddressAreEmpty() throws Exception {
        OrderShippingAndPaymentPage orderShippingAndPaymentPage = new OrderShippingAndPaymentPage(driver);
        driver.get(FrameworkConstants.SHIPMENT_AND_PAYMENT_URL);
        shippingAndPaymentData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH,TestExcelDataUtils.SHIPMENT_PAYMENT_DATA_SHEET);
        int recordNumber = 11; // Record number in the Excel file to be used for this test
        orderShippingAndPaymentPage.verifyOrderFailsWhenDistrictCommuneAndAddressAreEmpty(
                shippingAndPaymentData.getCellData(NAME,recordNumber),
                shippingAndPaymentData.getCellData(PHONE,recordNumber),
                shippingAndPaymentData.getCellData(EMAIL,recordNumber),
                shippingAndPaymentData.getCellData(CITY,recordNumber),
                shippingAndPaymentData.getCellData(PAYMENT_METHOD,recordNumber),
                shippingAndPaymentData.getCellData(COMMENT,recordNumber)
        );
    }

    @Test(
            testName = "TC_SP_10"
//            dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations", groups = {"ClearCookies"}
    )
    @Description("Create order without checking the terms and conditions checkbox")
    public void verifyOrderFailsWhenTermsAndConditionsNotChecked() throws Exception {
        OrderShippingAndPaymentPage orderShippingAndPaymentPage = new OrderShippingAndPaymentPage(driver);
        driver.get(FrameworkConstants.SHIPMENT_AND_PAYMENT_URL);
        shippingAndPaymentData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH,TestExcelDataUtils.SHIPMENT_PAYMENT_DATA_SHEET);
        int recordNumber = 12; // Record number in the Excel file to be used for this test
        orderShippingAndPaymentPage.verifyOrderFailsWhenTermsNotChecked(
                shippingAndPaymentData.getCellData(NAME,recordNumber),
                shippingAndPaymentData.getCellData(PHONE,recordNumber),
                shippingAndPaymentData.getCellData(EMAIL,recordNumber),
                shippingAndPaymentData.getCellData(CITY,recordNumber),
                shippingAndPaymentData.getCellData(DISTRICT,recordNumber),
                shippingAndPaymentData.getCellData(COMMUNE,recordNumber),
                shippingAndPaymentData.getCellData(DETAIL_ADDRESS,recordNumber),
                shippingAndPaymentData.getCellData(PAYMENT_METHOD,recordNumber),
                shippingAndPaymentData.getCellData(COMMENT,recordNumber)
        );
    }
}
