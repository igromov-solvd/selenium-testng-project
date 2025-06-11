package com.solvd.selenium.tests;

import com.solvd.selenium.pages.HomePage;
import com.solvd.selenium.pages.SearchResultsPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

public class ProductSearchTest extends BaseTest {

    @Test(description = "Search for a product and validate results")
    public void testProductSearch() {
        String searchTerm = "jeans";
        logger.info("Starting product search test for: {}", searchTerm);

        navigateToHomePage();
        HomePage homePage = new HomePage(getDriver());
        SearchResultsPage searchResultsPage = homePage.searchForProduct(searchTerm);

        List<String> productTitles = searchResultsPage.getProductTitles();
        Assert.assertFalse(productTitles.isEmpty(),
                "Product titles list should not be empty");

        searchResultsPage.printAllProductTitles();
        logger.info("Product search test completed successfully. Found {} products",
                productTitles.size());

        long relevantProducts = productTitles.stream()
                .filter(title -> title.toLowerCase().contains(searchTerm.toLowerCase()) ||
                        title.toLowerCase().contains("stretch") ||
                        title.toLowerCase().contains("slim"))
                .count();

        logger.info("Found {} relevant products out of {} total products",
                relevantProducts, productTitles.size());

        Assert.assertTrue(relevantProducts > 0,
                "Expected at least one product to match relevance criteria");
    }
}