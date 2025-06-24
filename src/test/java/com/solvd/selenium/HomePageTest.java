package com.solvd.selenium;

import com.solvd.selenium.pages.HomePage;
import java.util.Arrays;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class HomePageTest extends BaseTest {

    private static final List<String> EXPECTED_CATEGORIES = Arrays.asList(
            "women", "men", "boys", "girls", "home", "baby",
            "furniture", "holiday", "brands", "beauty", "gifts",
            "sports", "clearance");

    /**
     * NEXT_001 - Verify Homepage Load and Essential Elements
     * 
     * Test to verify essential elements on the homepage.
     * This includes checking the visibility of the delivery title,
     * store locator, help link, logo, search bar, account icon,
     * favorites icon, shopping bag icon, checkout button, and main menu.
     */
    @Test(description = "Verify homepage load and essential elements")
    public void testHomePageElements() {
        logger.info("Starting homepage elements verification test");

        navigateToHomePage();
        HomePage homePage = new HomePage(getDriver());

        verifyDeliverySection(homePage);
        verifyHeaderElements(homePage);
        verifyMainMenuCategories(homePage);

        logger.info("Homepage elements verification test completed successfully");
    }

    private void verifyDeliverySection(HomePage homePage) {
        Assert.assertTrue(homePage.isDeliveryTitleVisible(), "Delivery title should be visible");
        Assert.assertEquals(homePage.getDeliveryTitleText(),
                "Next day delivery to home or free to store*",
                "Delivery title text is incorrect");
    }

    private void verifyHeaderElements(HomePage homePage) {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(homePage.isStoreLocatorVisible(), "Store Locator link should be visible");
        softAssert.assertTrue(homePage.isHelpLinkVisible(), "Help link should be visible");
        softAssert.assertTrue(homePage.isLogoVisible(), "Next logo should be visible");
        softAssert.assertTrue(homePage.isSearchBarVisible(), "Search bar should be visible");
        softAssert.assertTrue(homePage.isAccountIconVisible(), "Account icon should be visible");
        softAssert.assertTrue(homePage.isFavoritesIconVisible(), "Favorites icon should be visible");
        softAssert.assertTrue(homePage.isShoppingBagIconVisible(), "Shopping bag icon should be visible");
        softAssert.assertTrue(homePage.isCheckoutButtonVisible(), "Checkout button should be visible");
        softAssert.assertAll();
    }

    private void verifyMainMenuCategories(HomePage homePage) {
        Assert.assertEquals(homePage.getMainMenuCategoriesList(), EXPECTED_CATEGORIES,
                "Main menu categories are incorrect");
    }
}