package com.browserstack.tests;

import com.browserstack.common.ExcelHelpers;
import constants.TestExcelDataUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ProductDetailPage;
import utils.DriverManager;

import static utils.DriverManager.clearBrowserData;

@Test(groups = {"ChooseItemVariations"})
@Listeners({com.browserstack.listeners.CustomExcecutionListener.class})
public class ChooseItemVariations extends SeleniumTest {

    // This class is intended to handle the selection of item variations
    // such as size, color, and quantity on a product detail page.
    // It will interact with the UI elements to choose the desired variations.

    private static final int TC_AC_1_DATA_ROW_OFFSET = 1; // Offset for the first data row in the Excel file
    private static final int TC_AC_1_DATA_RECORD_NUMBER = 3; // Offset for the first data row in the Excel file
    private static final int TC_AC_2_DATA_ROW_OFFSET = 4; // Offset for the first data row in the Excel file
    private static final int TC_AC_3_DATA_ROW_OFFSET = 5; // Offset for the first data row in the Excel file
    private static final int TC_AC_4_DATA_ROW_OFFSET = 6; // Offset for the first data row in the Excel file
    private static final int TC_AC_5_DATA_ROW_OFFSET = 1; // Offset for the first data row in the Excel file
    private static final int TC_AC_6_DATA_ROW_OFFSET = 1; // Offset for the first data row in the Excel file


    @BeforeMethod(alwaysRun = true)
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        driver = DriverManager.getInstance().getDriver();
//        clearBrowserData();
    }

    @AfterMethod(onlyForGroups = {"ClearCookies"})
    public void clearCookies() throws Exception {
        Thread.sleep(1000);
        clearBrowserData();
    }

    @AfterMethod( onlyForGroups = {"ClearCookies"})
    public void tearDown() throws Exception {
        driver = DriverManager.getInstance().getDriver();
        driver.quit();
    }


    @Test(testName = "TC_AC_1", invocationCount = 1
                   ,groups = {"NotClearCookies"}, alwaysRun = true
    )
    @Feature("Choose item variations")
    @Description("Add product to cart with variations")
    public static void addProductToCartWithVariations() throws Exception {
        ExcelHelpers productDetailPageData = new ExcelHelpers();
        // Set the Excel file and sheet for product detail data
        productDetailPageData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH, TestExcelDataUtils.PRODUCT_DETAIL_DATA_SHEET);
        // TODO: SET GLOBAL VARIABLES FOR RECORDS NUMBER AND OFFSET
        int numOfRecords = TC_AC_1_DATA_RECORD_NUMBER;
        int recordOffset = TC_AC_1_DATA_ROW_OFFSET;
        ProductDetailPage productDetailPage = new ProductDetailPage(driver);

        for (int i = 1; i < recordOffset + numOfRecords ; i++) {
            driver.get(productDetailPageData.getCellData(TestExcelDataUtils.PRODUCT_URL, i));
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

    @Test(testName = "TC_AC_2", groups = {"ClearCookies"})
    @Feature("Choose item variations")
    @Description("Verify that adding product to cart with variations fails after resetting variations")
    public void addToCartFailedAfterResetVariations() throws Exception {
        ExcelHelpers productDetailPageData = new ExcelHelpers();
        String testUrl = "https://theblues.com.vn/san-pham/thun-croptop-in-hinh-tvm-t2m-19-478/";
        // Set the Excel file and sheet for product detail data
        productDetailPageData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH, TestExcelDataUtils.PRODUCT_DETAIL_DATA_SHEET);
        int rowNumber = TC_AC_2_DATA_ROW_OFFSET; // Row number in the Excel file to be used for this test
        ProductDetailPage productDetailPage = new ProductDetailPage(driver);
        productDetailPage.addToCartFailedAfterResetVariations(testUrl,
                productDetailPageData.getCellData(TestExcelDataUtils.SIZE, rowNumber),
                productDetailPageData.getCellData(TestExcelDataUtils.QUANTITY, rowNumber),
                productDetailPageData.getCellData(TestExcelDataUtils.COLOR, rowNumber));
    }

    @Test(testName = "TC_AC_3", groups = {"ClearCookies"})
    @Feature("Choose item variations")
    @Description("Choose item with zero quantity")
    public void chooseItemWithZeroQuantity() throws Exception {
        ExcelHelpers productDetailPageData = new ExcelHelpers();
        String testUrl = "https://theblues.com.vn/san-pham/thun-croptop-in-hinh-tvm-t2m-19-478/";
        // Set the Excel file and sheet for product detail data
        productDetailPageData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH, TestExcelDataUtils.PRODUCT_DETAIL_DATA_SHEET);
        int rowNumber = TC_AC_3_DATA_ROW_OFFSET; // Row number in the Excel file to be used for this test
        ProductDetailPage productDetailPage = new ProductDetailPage(driver);
        productDetailPage.addToCartWithZeroQuantity(testUrl,
                productDetailPageData.getCellData(TestExcelDataUtils.SIZE, rowNumber),
                productDetailPageData.getCellData(TestExcelDataUtils.COLOR, rowNumber));
    }

    @Test(testName = "TC_AC_4", groups = {"ClearCookies"})
    @Feature("Choose item variations")
    @Description("Choose item with invalid quantity")
    public void chooseItemWithInvalidQuantity() throws Exception {
        ExcelHelpers productDetailPageData = new ExcelHelpers();
        String testUrl = "https://theblues.com.vn/san-pham/thun-croptop-in-hinh-tvm-t2m-19-478/";
        // Set the Excel file and sheet for product detail data
        productDetailPageData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH, TestExcelDataUtils.PRODUCT_DETAIL_DATA_SHEET);
        int rowNumber = TC_AC_4_DATA_ROW_OFFSET; // Row number in the Excel file to be used for this test
        ProductDetailPage productDetailPage = new ProductDetailPage(driver);
        productDetailPage.addToCartWithInvalidQuantity(testUrl,
                productDetailPageData.getCellData(TestExcelDataUtils.SIZE, rowNumber),
                productDetailPageData.getCellData(TestExcelDataUtils.COLOR, rowNumber),
                productDetailPageData.getCellData(TestExcelDataUtils.QUANTITY, rowNumber));
    }

}
