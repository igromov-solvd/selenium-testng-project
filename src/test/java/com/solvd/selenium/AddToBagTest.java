package com.solvd.selenium;

import com.solvd.selenium.pages.HomePage;
import com.solvd.selenium.pages.CategoryPage;
import com.solvd.selenium.pages.ProductPage;
import com.solvd.selenium.pages.ShoppingBagPage;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class AddToBagTest extends BaseTest {

    /**
     * NEXT_005 - Verify Add Product to Bag and View Bag
     *
     * Test to verify that a user can add a product to the shopping bag.
     * This includes navigating to a product, selecting size, adding to bag,
     * and verifying the bag contents.
     */
    @Test(description = "Verify add product to bag functionality")
    @Parameters({ "category", "subCategory" })
    public void testAddToBag(
            @Optional("women") String category,
            @Optional("All Dresses") String subCategory) {
        logger.info("Starting add to bag test");

        navigateToHomePage();
        HomePage homePage = new HomePage(getDriver());

        // Hover over Women category
        CategoryPage categoryPage = homePage.hoverOverMainCategoryAndClick(category, subCategory);
        Assert.assertNotNull(categoryPage, "Category page should not be null");

        ProductPage productPage = categoryPage.clickFirstProduct();

        // Select size and add to bag
        productPage.selectFirstAvailableSize();
        productPage.clickAddToBag();

        // Verify confirmation
        Assert.assertTrue(productPage.isAddToBagConfirmationVisible(),
                "Add to bag confirmation should be visible");

        // View bag from confirmation
        ShoppingBagPage shoppingBagPage = productPage.clickViewBagButton();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(shoppingBagPage.isFirstBagItemVisible(),
                "Bag item should be visible");
        softAssert.assertTrue(shoppingBagPage.isFirstBagItemPriceVisible(),
                "Price should be visible");
        softAssert.assertTrue(shoppingBagPage.isFirstBagItemQuantityVisible(),
                "Quantity should be visible");
        softAssert.assertTrue(shoppingBagPage.isFirstBagItemRemoveButtonVisible(),
                "Remove button should be visible");
        softAssert.assertAll();

        logger.info("Add to bag test completed successfully");
    }
}