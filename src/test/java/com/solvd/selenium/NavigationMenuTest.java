package com.solvd.selenium;

import com.solvd.selenium.pages.HomePage;
import com.solvd.selenium.pages.CategoryPage;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

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
    @Parameters({ "category", "subCategory", "expectedTitle" })
    public void testCategoryNavigation(
            @Optional("women") String category,
            @Optional("All Dresses") String subCategory,
            @Optional("women's dresses") String expectedTitle) {
        logger.info("Starting category navigation test for {} > {}", category, subCategory);

        navigateToHomePage();
        HomePage homePage = new HomePage(getDriver());

        // Hover over Women category
        CategoryPage categoryPage = homePage.hoverOverMainCategoryAndClick(category, subCategory);
        Assert.assertNotNull(categoryPage, "Category page should not be null");

        verifyCategoryPageElements(categoryPage, expectedTitle);
        logger.info("Category navigation test completed successfully");
    }

    private void verifyCategoryPageElements(CategoryPage categoryPage, String expectedTitle) {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(categoryPage.getPageTitle().toLowerCase(), expectedTitle.toLowerCase(),
                "Category page title should match expected title");
        softAssert.assertTrue(categoryPage.areProductsVisible(), "Products should be visible");
        softAssert.assertTrue(categoryPage.areFiltersVisible(), "Filters should be visible");
        softAssert.assertTrue(categoryPage.isSortingVisible(), "Sorting options should be visible");
        softAssert.assertAll();
    }
}