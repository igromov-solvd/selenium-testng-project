package com.solvd.selenium;

import com.solvd.selenium.pages.CategoryPage;
import com.solvd.selenium.pages.HomePage;
import com.solvd.selenium.pages.ProductPage;
import com.solvd.selenium.pages.ShoppingBagPage;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class UpdateQuantityTest extends BaseTest {

    /**
     * NEXT_007 - Verify Update Quantity in Bag
     *
     * Test to verify that a user can update the quantity of a product in the
     * shopping bag.
     * This includes starting from the shopping bag page, updating the quantity,
     * and verifying that the subtotal reflects the new quantity.
     */
    @Test(description = "Verify update quantity in bag functionality")
    @Parameters({ "category", "subCategory", "newQuantity" })
    public void testUpdateQuantity(
            @Optional("women") String category,
            @Optional("All Dresses") String subCategory,
            @Optional("2") int newQuantity) {
        logger.info("Starting update quantity test");

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

        // Get initial price
        double initialPrice = shoppingBagPage.getProductPrice();

        // Update quantity
        shoppingBagPage.clickOnQuantityCountPlus();

        // Verify updates
        Assert.assertEquals(shoppingBagPage.getQuantity(), newQuantity,
                "Quantity should be updated");

        // Verify total price reflects new quantity
        shoppingBagPage.waitForTotalPriceChanged(initialPrice * newQuantity);

        logger.info("Update quantity test completed successfully");
    }
}