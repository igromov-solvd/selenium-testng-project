package com.solvd.selenium;

import com.solvd.selenium.pages.HomePage;
import com.solvd.selenium.pages.CategoryPage;
import com.solvd.selenium.pages.ProductPage;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ProductDetailPageTest extends BaseTest {

    /**
     * NEXT_004 - Verify Product Detail Page - View Product Information
     *
     * Test to verify that the product detail page displays all necessary
     * information
     * about a product, including name, price, description, images, size and color
     * options,
     * add to bag button, favorite button, and reviews section.
     */
    @Test(description = "Verify product detail page information")
    @Parameters({ "category", "subCategory" })
    public void testProductDetailPage(
            @Optional("women") String category,
            @Optional("All Dresses") String subCategory) {
        logger.info("Starting product detail page test");

        navigateToHomePage();
        HomePage homePage = new HomePage(getDriver());

        // Hover over Women category
        CategoryPage categoryPage = homePage.hoverOverMainCategoryAndClick(category, subCategory);
        Assert.assertNotNull(categoryPage, "Category page should not be null");

        // Click on first product
        ProductPage productPage = categoryPage.clickFirstProduct();

        verifyProductDetails(productPage);
        logger.info("Product detail page test completed successfully");
    }

    private void verifyProductDetails(ProductPage productPage) {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(productPage.isProductNameVisible(), "Product name should be visible");
        softAssert.assertTrue(productPage.isProductPriceVisible(), "Product price should be visible");
        softAssert.assertTrue(productPage.isProductDescriptionVisible(), "Product description should be visible");
        softAssert.assertTrue(productPage.areProductImagesVisible(), "Product images should be visible");
        softAssert.assertTrue(productPage.isSizeSelectionVisible(), "Size selection should be available");
        softAssert.assertTrue(productPage.isColorSelectionVisible(), "Color selection should be available");
        softAssert.assertTrue(productPage.isAddToBagButtonVisible(), "Add to Bag button should be visible");
        softAssert.assertTrue(productPage.isFavouriteButtonVisible(), "Favourite button should be visible");
        softAssert.assertTrue(productPage.hasReviewsSection(), "Reviews section should be visible if available");
        softAssert.assertAll();
    }
}