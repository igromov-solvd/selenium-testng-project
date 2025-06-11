package com.solvd.selenium.tests;

import com.solvd.selenium.pages.HomePage;
import com.solvd.selenium.pages.SearchResultsPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

public class ProductSearchTest extends BaseTest {

    @Test(description = "Search for a product and validate results")
    public void testProductSearch() {
        // Define the search term
        String searchTerm = "jeans";

        logger.info("Starting product search test for: {}", searchTerm);

        // Navigate to home page
        navigateToHomePage();

        // Initialize page objects
        HomePage homePage = new HomePage(driver);

        // Perform search
        SearchResultsPage searchResultsPage = homePage.searchForProduct(searchTerm);

        // Get and validate product titles
        List<String> productTitles = searchResultsPage.getProductTitles();
        Assert.assertFalse(productTitles.isEmpty(),
                "Product titles list should not be empty");

        // Print all product titles to console
        searchResultsPage.printAllProductTitles();

        // Log test completion
        logger.info("Product search test completed successfully. Found {} products",
                productTitles.size());

        // Additional validations
        Assert.assertTrue(productTitles.size() > 0,
                "Should find at least one product");

        // Verify that product titles contain relevant keywords (case-insensitive)
        long relevantProducts = productTitles.stream()
                .filter(title -> title.toLowerCase().contains(searchTerm.toLowerCase()) ||
                        title.toLowerCase().contains("stretch") ||
                        title.toLowerCase().contains("slim"))
                .count();

        logger.info("Found {} relevant products out of {} total products",
                relevantProducts, productTitles.size());
    }
}