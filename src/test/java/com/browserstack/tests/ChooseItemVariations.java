package com.browserstack.tests;

import com.browserstack.common.ExcelHelpers;
import constants.TestExcelDataUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ProductDetailPage;

@Listeners({com.browserstack.listeners.CustomExcecutionListener.class})
@Test()
public class ChooseItemVariations extends SeleniumTest {

    // This class is intended to handle the selection of item variations
    // such as size, color, and quantity on a product detail page.
    // It will interact with the UI elements to choose the desired variations.

    ExcelHelpers productDetailPageData = new ExcelHelpers();

    @Test(testName = "Add product to cart with variations", invocationCount = 1,groups = "NotClearCookies")
    @Feature("Choose item variations")
    @Description("Add product to cart with variations")
    public void addProductToCartWithVariations() throws Exception {
        String testUrl = "https://theblues.com.vn/san-pham/ao-so-mi-nam-hoa-tiet-hk1-ms22n023ubr4/";
        driver.get(testUrl);
        // Set the Excel file and sheet for product detail data
        productDetailPageData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH, TestExcelDataUtils.PRODUCT_DETAIL_DATA_SHEET);
        int numOfRecords = 2;
        int recordOffset = 1;
        ProductDetailPage productDetailPage = new ProductDetailPage(driver);

        for (int i = 1; i < recordOffset + numOfRecords ; i++) {
            productDetailPage.addCardFluentActionsTest(
                    productDetailPageData.getCellData(TestExcelDataUtils.SIZE, i),
                    productDetailPageData.getCellData(TestExcelDataUtils.QUANTITY, i),
                    productDetailPageData.getCellData(TestExcelDataUtils.COLOR, i));
            productDetailPage.clickAddToCartButton();
            if(i < recordOffset + numOfRecords - 1) {
                productDetailPage.closeCheckoutDialog();
            }
        }
        productDetailPage.navigateToViewCartPage();
    }

    @Test(groups = "ChooseItemVariations")
    @Feature("Choose item variations")
    @Description("Create order of an item with variations")
    public void testTB1() throws Exception {
        String testUrl = "https://theblues.com.vn/san-pham/thun-croptop-in-hinh-tvm-t2m-19-478/";
        // Set the Excel file and sheet for product detail data
        productDetailPageData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH, TestExcelDataUtils.PRODUCT_DETAIL_DATA_SHEET);
        int rowNumber = 2; // Row number in the Excel file to be used for this test
        ProductDetailPage productDetailPage = new ProductDetailPage(driver);
        productDetailPage.addToCartFailedAfterResetVariations(testUrl,
                productDetailPageData.getCellData(TestExcelDataUtils.SIZE, rowNumber),
                productDetailPageData.getCellData(TestExcelDataUtils.QUANTITY, rowNumber),
                productDetailPageData.getCellData(TestExcelDataUtils.COLOR, rowNumber));
    }

    @Test(groups = "ChooseItemVariations")
    @Feature("Choose item variations")
    @Description("Choose item with zero quantity")
    public void chooseItemWithZeroQuantity() throws Exception {
        String testUrl = "https://theblues.com.vn/san-pham/thun-croptop-in-hinh-tvm-t2m-19-478/";
        // Set the Excel file and sheet for product detail data
        productDetailPageData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH, TestExcelDataUtils.PRODUCT_DETAIL_DATA_SHEET);
        int rowNumber = 2; // Row number in the Excel file to be used for this test
        ProductDetailPage productDetailPage = new ProductDetailPage(driver);
        productDetailPage.addToCartWithZeroQuantity(testUrl,
                productDetailPageData.getCellData(TestExcelDataUtils.SIZE, rowNumber),
                productDetailPageData.getCellData(TestExcelDataUtils.COLOR, rowNumber));
    }

      @Test(groups = "ChooseItemVariations")

    @Feature("Choose item variations")
    @Description("Choose item with invalid quantity")
    public void chooseItemWithInvalidQuantity() throws Exception {
        String testUrl = "https://theblues.com.vn/san-pham/thun-croptop-in-hinh-tvm-t2m-19-478/";
        // Set the Excel file and sheet for product detail data
        productDetailPageData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH, TestExcelDataUtils.PRODUCT_DETAIL_DATA_SHEET);
        int rowNumber = 3; // Row number in the Excel file to be used for this test
        ProductDetailPage productDetailPage = new ProductDetailPage(driver);
        productDetailPage.addToCartWithInvalidQuantity(testUrl,
                productDetailPageData.getCellData(TestExcelDataUtils.SIZE, rowNumber),
                productDetailPageData.getCellData(TestExcelDataUtils.COLOR, rowNumber),
                productDetailPageData.getCellData(TestExcelDataUtils.QUANTITY, rowNumber));
    }

}
