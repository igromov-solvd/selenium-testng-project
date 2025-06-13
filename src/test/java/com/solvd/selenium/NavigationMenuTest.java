package com.solvd.selenium;

import com.solvd.selenium.pages.HomePage;
import com.solvd.selenium.pages.CategoryPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NavigationMenuTest extends BaseTest {

    /**
     * NEXT_003 - Verify Navigation Menu - Category Page Load
     *
     * Test to verify that the navigation menu allows users to navigate to a
     * category page.
     * This includes checking the visibility of the sub-menu, clicking on a
     * sub-category,
     * and verifying that the category page loads with products, filters, and
     * sorting options.
     */
    @Test(description = "Verify navigation menu category page load")
    public void testCategoryNavigation() {
        String category = "Women";
        String subCategory = "All Dresses";
        logger.info("Starting category navigation test for {} > {}", category, subCategory);

        navigateToHomePage();
        HomePage homePage = new HomePage(getDriver());

        // Hover over Women category
        CategoryPage categoryPage = homePage.hoverOverMainCategoryAndClick(category, subCategory);
        Assert.assertNotNull(categoryPage, "Category page should not be null");

        // Verify category page
        Assert.assertEquals(categoryPage.getPageTitle().toLowerCase(), "women's dresses",
                "Category page title should match expected title");
        Assert.assertTrue(categoryPage.areProductsVisible(), "Products should be visible");
        Assert.assertTrue(categoryPage.areFiltersVisible(), "Filters should be visible");
        Assert.assertTrue(categoryPage.isSortingVisible(), "Sorting options should be visible");

        logger.info("Category navigation test completed successfully");
    }
}