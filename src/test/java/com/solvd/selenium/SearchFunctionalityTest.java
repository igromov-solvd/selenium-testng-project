package com.solvd.selenium;

import com.solvd.selenium.pages.HomePage;
import com.solvd.selenium.pages.SearchResultsPage;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchFunctionalityTest extends BaseTest {

    /**
     * NEXT_002 - Verify Search Functionality - Valid Product
     *
     * Test to verify the search functionality on the homepage.
     * This includes searching for a valid product and checking
     * the search results page for relevant products, filters, and sorting options.
     */
    @Test(description = "Verify search functionality with valid product")
    public void testValidProductSearch() {
        String searchTerm = "dress";
        logger.info("Starting search functionality test for: {}", searchTerm);

        navigateToHomePage();
        HomePage homePage = new HomePage(getDriver());
        SearchResultsPage searchResultsPage = homePage.searchForProduct(searchTerm);

        // Verify search results page
        Assert.assertTrue(searchResultsPage.getPageTitleText().toLowerCase().contains(searchTerm),
                "Search results title should contain search term");

        // Verify product details visibility
        List<String> productTitles = searchResultsPage.getProductTitles();
        Assert.assertFalse(productTitles.isEmpty(), "Product titles should not be empty");

        // Verify filters and sorting options
        Assert.assertTrue(searchResultsPage.areFiltersVisible(), "Filters should be visible");
        Assert.assertTrue(searchResultsPage.isSortingVisible(), "Sorting options should be visible");

        logger.info("Search functionality test completed successfully");
    }
}
