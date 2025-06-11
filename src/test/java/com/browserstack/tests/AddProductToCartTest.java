package com.browserstack.tests;

import com.browserstack.common.ExcelHelpers;
import constants.TestExcelDataUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OrderShippingAndPaymentPage;
import pages.ProductDetailPage;
import pages.ViewCartPage;

import static constants.TestExcelDataUtils.*;

@Listeners({com.browserstack.listeners.CustomExcecutionListener.class})
@Test(groups = "AddProductToCartTest")
public class AddProductToCartTest extends SeleniumTest{

    @Test
    @Feature("Create order")
    @Description("Create order of an item with variations")
    @Owner("normdevstorm")
    void test(){
        driver.get("https://theblues.com.vn/san-pham/thun-croptop-in-hinh-tvm-t2m-19-478/");
        ExcelHelpers dataProductDetailPage = new ExcelHelpers();
        ExcelHelpers dataViewCartPage = new ExcelHelpers();
        ExcelHelpers dataShipmentPaymentPage = new ExcelHelpers();
        ExcelHelpers dataOrderListPage = new ExcelHelpers();

        int testRowNumber = 2; // Row number in the Excel file to be used for this test

        try {
            dataProductDetailPage.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH, TestExcelDataUtils.PRODUCT_DETAIL_DATA_SHEET);
            dataViewCartPage.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH, TestExcelDataUtils.VIEW_CART_DATA_SHEET);
            dataShipmentPaymentPage.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH, TestExcelDataUtils.SHIPMENT_PAYMENT_DATA_SHEET);
            dataOrderListPage.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH, TestExcelDataUtils.ORDER_LIST_DATA_SHEET);

            Allure.step("Choose item options");
            ProductDetailPage productDetailPage = new ProductDetailPage(driver);
            productDetailPage.addCardFluentActionsTest( dataProductDetailPage.getCellData(SIZE, testRowNumber), dataProductDetailPage.getCellData(QUANTITY, testRowNumber), dataProductDetailPage.getCellData(COLOR, testRowNumber));
//            productDetailPage.resetVariations();
            productDetailPage.clickAddToCartButton();
            Allure.step("View cart and modify shipping address");
            ViewCartPage viewCartPage = productDetailPage.navigateToViewCartPage();
            viewCartPage.modifyShippingAddress(dataViewCartPage.getCellData(COUNTRY, testRowNumber),
                                                dataViewCartPage.getCellData(CITY, testRowNumber), dataViewCartPage.getCellData(DISTRICT, testRowNumber));
            Allure.step("Review order info and choose payment method");
            OrderShippingAndPaymentPage orderShippingAndPaymentPage = viewCartPage.navigateToOrderShippingAndPaymentPage();
            orderShippingAndPaymentPage.completeCheckoutWithFullAddress(dataShipmentPaymentPage.getCellData(NAME,testRowNumber), dataShipmentPaymentPage.getCellData(PHONE,testRowNumber), dataShipmentPaymentPage.getCellData(EMAIL,testRowNumber),   dataShipmentPaymentPage.getCellData(CITY,testRowNumber),  dataShipmentPaymentPage.getCellData(DISTRICT,testRowNumber),  dataShipmentPaymentPage.getCellData(COMMUNE,testRowNumber), dataShipmentPaymentPage.getCellData(DETAIL_ADDRESS,testRowNumber), dataShipmentPaymentPage.getCellData(PAYMENT_METHOD,testRowNumber), dataShipmentPaymentPage.getCellData(COMMENT,testRowNumber));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
