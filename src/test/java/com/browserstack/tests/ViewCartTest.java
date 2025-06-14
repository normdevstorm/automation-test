package com.browserstack.tests;

import com.browserstack.common.ExcelHelpers;
import com.browserstack.listeners.CustomExcecutionListener;
import constants.FrameworkConstants;
import constants.TestExcelDataUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import models.ItemPriceModel;
import org.slf4j.Logger;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ViewCartPage;
import utils.DriverManager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static utils.DriverManager.clearBrowserData;

@Listeners({CustomExcecutionListener.class})
@Test(groups = {"ViewCartTest"})
public class ViewCartTest extends  SeleniumTest {

    ExcelHelpers viewCartPageData = new ExcelHelpers();
    ExcelHelpers productDetailPageData = new ExcelHelpers();
    Logger log = org.slf4j.LoggerFactory.getLogger(ViewCartTest.class);

    private final static int TC_VC_6_DATA_ROW_OFFSET = 8; // Offset for the first data row in the Excel file
    private final static int TC_AC_DATA_ROW_OFFSET = 1; // Offset for the first data row in the Excel file
    private final static int TC_AC_DATA_NUM_RECORDS = 3; // Offset for the first data row in the Excel file

    @BeforeMethod(alwaysRun = true, firstTimeOnly = true
    )
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        driver = DriverManager.getInstance().getDriver();
//        clearBrowserData();
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


    @Test(testName = "TC_VC_1",
            groups = {"ClearCookies"})
    @Feature("View Cart")
    @Description("View cart and verify items")
    public static void verifySucceedDeleteItemAndRestore() throws Exception {
        driver.get(FrameworkConstants.VIEW_CART_URL);
        ViewCartPage viewCartPage = new ViewCartPage(driver);
        viewCartPage.deleteAndRestoreCartItem(1);
    }

    @Test(testName = "TC_VC_2",
//            dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations",
            groups = {"ClearCookies"})
    @Feature("View Cart")
    @Description("Set quantity of an item to 0")
    public void verifySucceedSetQuantityToZero() throws Exception {
        driver.get(FrameworkConstants.VIEW_CART_URL);
        ViewCartPage viewCartPage = new ViewCartPage(driver);
        viewCartPage.setItemQuantityToZeroToDelete(1);
    }

    @Test(testName = "TC_VC_3",
            groups = {"ClearCookies"})
    @Feature("View Cart")
    @Description("Delete all items from cart")
    public void verifySucceedDeleteAllItemsFromCart() throws Exception {
        driver.get(FrameworkConstants.VIEW_CART_URL);
        ViewCartPage viewCartPage = new ViewCartPage(driver);
        viewCartPage.deleteAllItemsFromCart();
    }

    @Test(testName = "TC_VC_4",
            groups = "ClearCookies")
    @Feature(("View Cart"))
    @Description("Set initial shipping address")
    public void verifySucceedSetShippingAddress() throws Exception {
        driver.get(FrameworkConstants.VIEW_CART_URL);
        ViewCartPage viewCartPage = new ViewCartPage(driver);
        int rowIndex = 4;
        viewCartPageData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH, TestExcelDataUtils.VIEW_CART_DATA_SHEET);
        viewCartPage.modifyShippingAddress(
                viewCartPageData.getCellData(TestExcelDataUtils.COUNTRY, rowIndex),
                viewCartPageData.getCellData(TestExcelDataUtils.CITY, rowIndex),
                viewCartPageData.getCellData(TestExcelDataUtils.DISTRICT, rowIndex));

    }

    @Test(testName = "TC_VC_5",
            invocationCount = 3, groups = {"ClearCookies"})
    @Feature(("View Cart"))
    @Description("Modify shipping address")
    public void verifySucceedModifyShippingAddress() throws Exception {
        ITestNGMethod method = Reporter.getCurrentTestResult().getMethod();
        int rowIndex = method.getCurrentInvocationCount() + 4;
        if (rowIndex == 4) {
            driver.get(FrameworkConstants.VIEW_CART_URL);
        }
        ViewCartPage viewCartPage = new ViewCartPage(driver);

        log.info("Current invocation count: " + method.getCurrentInvocationCount());
        viewCartPageData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH, TestExcelDataUtils.VIEW_CART_DATA_SHEET);
        viewCartPage.modifyShippingAddress(
                viewCartPageData.getCellData(TestExcelDataUtils.COUNTRY, rowIndex),
                viewCartPageData.getCellData(TestExcelDataUtils.CITY, rowIndex),
                viewCartPageData.getCellData(TestExcelDataUtils.DISTRICT, rowIndex));

    }

    @Test(testName = "TC_VC_6",
            groups = {"ClearCookies"})
    @Feature(("View Cart"))
    @Description("Verify cart total")
    public void verifySucceedCartTotal() throws Exception {
        driver.get(FrameworkConstants.VIEW_CART_URL);
        viewCartPageData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH, TestExcelDataUtils.VIEW_CART_DATA_SHEET);
        productDetailPageData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH, TestExcelDataUtils.PRODUCT_DETAIL_DATA_SHEET);

        List<ItemPriceModel> productLists = new ArrayList<>();
        String shippingRate = viewCartPageData.getCellData(TestExcelDataUtils.SHIPPING_RATE, TC_VC_6_DATA_ROW_OFFSET);

        for (int i = 0; i < TC_AC_DATA_NUM_RECORDS; i++) {
            String itemName = productDetailPageData.getCellData(TestExcelDataUtils.PRODUCT_NAME, i + TC_AC_DATA_ROW_OFFSET);
            String itemPrice = productDetailPageData.getCellData(TestExcelDataUtils.PRODUCT_PRICE, i + TC_AC_DATA_ROW_OFFSET);
            String itemQuantity = productDetailPageData.getCellData(TestExcelDataUtils.QUANTITY, i + TC_AC_DATA_ROW_OFFSET);

            productLists.add(new ItemPriceModel(itemName, itemPrice, itemQuantity));
        }

        ViewCartPage viewCartPage = new ViewCartPage(driver);

        viewCartPage.verifyOrderPriceSucceed(productLists, shippingRate );
    }
}
