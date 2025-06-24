package com.solvd.selenium;

import com.solvd.selenium.pages.CategoryPage;
import com.solvd.selenium.pages.HomePage;
import com.solvd.selenium.pages.ProductPage;
import com.solvd.selenium.pages.ShoppingBagPage;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class RemoveFromBagTest extends BaseTest {

    /**
     * NEXT_006 - Verify Remove Product from Bag
     *
     * Test to verify that a user can remove a product from the shopping bag.
     * This includes starting from the shopping bag page, removing an item,
     * and verifying that the bag is empty.
     */
    @Test(description = "Verify remove product from bag functionality")
    @Parameters({ "category", "subCategory" })

    public void testRemoveFromBag(
            @Optional("women") String category,
            @Optional("All Dresses") String subCategory) {

        logger.info("Starting remove from bag test");

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

        // View bag fronm confirmation
        ShoppingBagPage shoppingBagPage = productPage.clickViewBagButton();

        // Remove item
        shoppingBagPage.clickFirstBagItemRemoveButton();

        verifyEmptyBagState(shoppingBagPage);

        // Continue shopping
        shoppingBagPage.clickContinueShopping();

        // Verify navigation back to homepage
        Assert.assertTrue(getDriver().getCurrentUrl().contains(config.getBaseUrl()),
                "Should be back on homepage after continuing shopping");

        logger.info("Remove from bag test completed successfully");
    }

    private void verifyEmptyBagState(ShoppingBagPage shoppingBagPage) {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(shoppingBagPage.isEmptyBagMessageVisible(),
                "Bag should be empty after removal");
        softAssert.assertEquals(shoppingBagPage.getEmptyBagMessageText(), "Your bag is empty",
                "Empty bag message is incorrect");
        softAssert.assertTrue(shoppingBagPage.isContinueShoppingVisible(),
                "`Continue Shopping` button should be visible");
        softAssert.assertAll();
    }
}