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
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ViewCartPage;

import java.time.Duration;
@Test(groups = "ViewCartTest")
@Listeners({CustomExcecutionListener.class})
public class ViewCartTest extends  SeleniumTest {

    ExcelHelpers viewCartPageData = new ExcelHelpers();
    Logger log = org.slf4j.LoggerFactory.getLogger(ViewCartTest.class);

    @Test(dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations", invocationCount = 1)
    @Feature("Add items for delete and restore")
    public void addManyItemsForDeleteAndRestore() throws Exception {
    }



    @Test(dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations")
    @Feature("View Cart")
    @Description("View cart and verify items")
    public void deleteItemAndRestore() throws Exception {
        driver.get(FrameworkConstants.VIEW_CART_URL);
        ViewCartPage viewCartPage = new ViewCartPage(driver);
        viewCartPage.deleteAndRestoreCartItem(1);
    }

    @Test(dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations")
    @Feature("View Cart")
    @Description("Set quantity of an item to 0")
    public void setQuantityToZero() throws Exception {
        driver.get(FrameworkConstants.VIEW_CART_URL);
        ViewCartPage viewCartPage = new ViewCartPage(driver);
        viewCartPage.setItemQuantityToZeroToDelete(1);
    }

    @Test(dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations")
    @Feature("View Cart")
    @Description("Delete all items from cart")
    public void deleteAllItemsFromCart() throws Exception {
        driver.get(FrameworkConstants.VIEW_CART_URL);
        ViewCartPage viewCartPage = new ViewCartPage(driver);
        viewCartPage.deleteAllItemsFromCart();
    }

    @Test(dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations")
    @Feature(("View Cart"))
    @Description("Set initial shipping address")
    public void setShippingAddress() throws Exception {
        driver.get(FrameworkConstants.VIEW_CART_URL);
        ViewCartPage viewCartPage = new ViewCartPage(driver);
        int rowNumber = 3;
        viewCartPageData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH, TestExcelDataUtils.VIEW_CART_DATA_SHEET);
        viewCartPage.modifyShippingAddress(
                viewCartPageData.getCellData(TestExcelDataUtils.COUNTRY, rowNumber),
                viewCartPageData.getCellData(TestExcelDataUtils.CITY, rowNumber),
                viewCartPageData.getCellData(TestExcelDataUtils.DISTRICT, rowNumber));

    }

    @Test(dependsOnMethods = "com.browserstack.tests.ChooseItemVariations.addProductToCartWithVariations", invocationCount = 3)
    @Feature(("View Cart"))
    @Description("Modify shipping address")
    public void modifyShippingAddress() throws Exception {
        ITestNGMethod method = Reporter.getCurrentTestResult().getMethod();
        int rowNumber = method.getCurrentInvocationCount() + 1;
        if(rowNumber == 1) {
            driver.get(FrameworkConstants.VIEW_CART_URL);
        }
        ViewCartPage viewCartPage = new ViewCartPage(driver);

        log.info("Current invocation count: " + method.getCurrentInvocationCount());
        viewCartPageData.setExcelFile(TestExcelDataUtils.ORDER_DATA_PATH, TestExcelDataUtils.VIEW_CART_DATA_SHEET);
            viewCartPage.modifyShippingAddress(
                    viewCartPageData.getCellData(TestExcelDataUtils.COUNTRY, rowNumber),
                    viewCartPageData.getCellData(TestExcelDataUtils.CITY, rowNumber),
                    viewCartPageData.getCellData(TestExcelDataUtils.DISTRICT, rowNumber));

    }
}
