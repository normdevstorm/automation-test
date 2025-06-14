package com.browserstack.tests;

import com.browserstack.common.ExcelHelpers;
import com.browserstack.listeners.CustomExcecutionListener;
import constants.FrameworkConstants;
import constants.TestExcelDataUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
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

import static utils.DriverManager.clearBrowserData;

@Listeners({CustomExcecutionListener.class})
@Test(groups = {"ViewCartTest"})
public class ViewCartTest extends  SeleniumTest {

    ExcelHelpers viewCartPageData = new ExcelHelpers();
    Logger log = org.slf4j.LoggerFactory.getLogger(ViewCartTest.class);

    @BeforeMethod(alwaysRun = true , firstTimeOnly = true
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
//        clearBrowserData();
    }

    @AfterMethod(alwaysRun = true, lastTimeOnly = true
//            , onlyForGroups = {"QuitDriver"}
    )
    public void tearDown() throws Exception {
        driver = DriverManager.getInstance().getDriver();
        driver.quit();
    }



    @Test( testName = "TC_VC_1",
            groups = {"ClearCookies"})
    @Feature("View Cart")
    @Description("View cart and verify items")
    public static void verifySucceedDeleteItemAndRestore() throws Exception {
        driver.get(FrameworkConstants.VIEW_CART_URL);
        ViewCartPage viewCartPage = new ViewCartPage(driver);
        viewCartPage.deleteAndRestoreCartItem(1);
    }

    @Test( testName = "TC_VC_2",
//            dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations",
            groups = {"ClearCookies"})
    @Feature("View Cart")
    @Description("Set quantity of an item to 0")
    public void verifySucceedSetQuantityToZero() throws Exception {
        driver.get(FrameworkConstants.VIEW_CART_URL);
        ViewCartPage viewCartPage = new ViewCartPage(driver);
        viewCartPage.setItemQuantityToZeroToDelete(1);
    }

    @Test( testName = "TC_VC_3",
            groups = {"ClearCookies"})
    @Feature("View Cart")
    @Description("Delete all items from cart")
    public void verifySucceedDeleteAllItemsFromCart() throws Exception {
        driver.get(FrameworkConstants.VIEW_CART_URL);
        ViewCartPage viewCartPage = new ViewCartPage(driver);
        viewCartPage.deleteAllItemsFromCart();
    }

    @Test( testName = "TC_VC_4",
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

    @Test( testName = "TC_VC_5",
            invocationCount = 3, groups = {"ClearCookies"})
    @Feature(("View Cart"))
    @Description("Modify shipping address")
    public void verifySucceedModifyShippingAddress() throws Exception {
        ITestNGMethod method = Reporter.getCurrentTestResult().getMethod();
        int rowIndex = method.getCurrentInvocationCount() + 4;
        if(rowIndex == 4) {
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
}
