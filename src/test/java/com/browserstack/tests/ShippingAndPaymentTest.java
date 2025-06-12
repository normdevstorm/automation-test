package com.browserstack.tests;

import com.browserstack.common.ExcelHelpers;
import constants.FrameworkConstants;
import constants.TestExcelDataUtils;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import pages.OrderShippingAndPaymentPage;

import static constants.TestExcelDataUtils.*;

public class ShippingAndPaymentTest extends SeleniumTest{

    ExcelHelpers shippingAndPaymentData = new ExcelHelpers();

   @Test(dependsOnMethods = {
              "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations",
           "com.browserstack.tests.ViewCartTest.setShippingAddress"}, groups = {"ClearCookies"})
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
//            dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations"
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
            //dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations"
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
            //dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations"
    )
    @Description("Create order with invalid phone field")
    public void verifyOrderFailsWhenPhoneIsInvalid() throws Exception {
        OrderShippingAndPaymentPage orderShippingAndPaymentPage = new OrderShippingAndPaymentPage(driver);
        driver.get(FrameworkConstants.SHIPMENT_AND_PAYMENT_URL);
        shippingAndPaymentData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH,TestExcelDataUtils.SHIPMENT_PAYMENT_DATA_SHEET);
        int recordNumber = 8; // Record number in the Excel file to be used for this test
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
            //dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations"
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
            //dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations"
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
//            dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations"
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

}
