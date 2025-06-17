package com.solvd.selenium;

import com.solvd.selenium.pages.CategoryPage;
import com.solvd.selenium.pages.HomePage;
import com.solvd.selenium.pages.ProductPage;
import com.solvd.selenium.pages.ShoppingBagPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RemoveFromBagTest extends BaseTest {

    /**
     * NEXT_006 - Verify Remove Product from Bag
     *
     * Test to verify that a user can remove a product from the shopping bag.
     * This includes starting from the shopping bag page, removing an item,
     * and verifying that the bag is empty.
     */
    @Test(description = "Verify remove product from bag functionality")
    public void testRemoveFromBag() {
        logger.info("Starting remove from bag test");

        String category = "Women";
        String subCategory = "All Dresses";

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

        // Verify removal
        Assert.assertTrue(shoppingBagPage.isEmptyBagMessageVisible(),
                "Bag should be empty after removal");
        Assert.assertEquals(shoppingBagPage.getEmptyBagMessageText(), "Your bag is empty",
                "Empty bag message is incorrect");
        Assert.assertTrue(shoppingBagPage.isContinueShoppingVisible(),
                "`Continue Shopping` button should be visible");

        // Continue shopping
        shoppingBagPage.clickContinueShopping();

        // Verify navigation back to homepage
        Assert.assertTrue(getDriver().getCurrentUrl().contains(config.getBaseUrl()),
                "Should be back on homepage after continuing shopping");

        logger.info("Remove from bag test completed successfully");
    }
}