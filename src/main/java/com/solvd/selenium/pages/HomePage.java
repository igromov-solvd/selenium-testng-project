package com.solvd.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends BasePage {

    @FindBy(css = "input#header-big-screen-search-box")
    private WebElement searchInput;

    @FindBy(css = "button[data-testid='header-search-bar-button']")
    private WebElement searchButton;

    @FindBy(css = "button#onetrust-accept-btn-handler")
    private WebElement acceptCookiesButton;

    @FindBy(css = "a[href='/delivery-service-local-store'] > span")
    private WebElement deliveryTitle;

    @FindBy(css = "a[href='https://www.next.co.uk/storelocator']")
    private WebElement storeLocatorLink;

    @FindBy(css = "a[href='https://www.next.co.uk/help']")
    private WebElement helpLink;

    @FindBy(css = "div[data-testid='header-adaptive-brand']")
    private WebElement logo;

    @FindBy(css = "div[data-testid='header-big-screen-search']")
    private WebElement searchBar;

    @FindBy(css = "div[data-testid='header-adaptive-my-account']")
    private WebElement accountIcon;

    @FindBy(css = "div[data-testid='header-favourites']")
    private WebElement favoritesIcon;

    @FindBy(css = "div[data-testid='header-shopping-bag']")
    private WebElement shoppingBagIcon;

    @FindBy(css = "div[data-testid='header-adaptive-checkout']")
    private WebElement checkoutButton;

    @FindBy(css = "ul[data-testid='snail-trail-container'] > li:not([style*='none']):not(.hideWideView)")
    private List<WebElement> mainMenuItems;

    @FindBy(css = "a[title='All Dresses']")
    private WebElement allDressesLink;

    public HomePage(WebDriver driver) {
        super(driver);
        acceptCookiesIfPresent();
        waitForPageLoad();
    }

    public void acceptCookiesIfPresent() {
        try {
            WebElement cookiesButton = wait.until(ExpectedConditions.elementToBeClickable(acceptCookiesButton));
            cookiesButton.click();
            logger.info("Cookies accepted");
        } catch (Exception e) {
            logger.info("No cookies banner found or already accepted");
        }
    }

    public SearchResultsPage clickSearchButton() {
        clickElement(searchButton);
        return new SearchResultsPage(driver);
    }

    public SearchResultsPage searchForProduct(String searchText) {
        enterText(searchInput, searchText);
        logger.info("Searching for product: {}", searchText);
        return clickSearchButton();
    }

    public boolean isDeliveryTitleVisible() {
        return isElementVisible(deliveryTitle);
    }

    public String getDeliveryTitleText() {
        return getElementText(deliveryTitle);
    }

    public boolean isStoreLocatorVisible() {
        return isElementVisible(storeLocatorLink);
    }

    public boolean isHelpLinkVisible() {
        return isElementVisible(helpLink);
    }

    public boolean isLogoVisible() {
        return isElementVisible(logo);
    }

    public boolean isSearchBarVisible() {
        return isElementVisible(searchBar);
    }

    public boolean isAccountIconVisible() {
        return isElementVisible(accountIcon);
    }

    public boolean isFavoritesIconVisible() {
        return isElementVisible(favoritesIcon);
    }

    public boolean isShoppingBagIconVisible() {
        return isElementVisible(shoppingBagIcon);
    }

    public boolean isCheckoutButtonVisible() {
        return isElementVisible(checkoutButton);
    }

    public boolean isMainMenuVisible() {
        return !mainMenuItems.isEmpty() && mainMenuItems.get(0).isDisplayed();
    }

    public List<String> getMainMenuCategoriesList() {
        List<String> actualCategories = new ArrayList<>();
        for (WebElement item : mainMenuItems) {
            actualCategories.add(item.getText().toLowerCase());
        }
        return actualCategories;
    }

    public CategoryPage hoverOverMainCategoryAndClick(String category, String subCategory) {
        for (WebElement item : mainMenuItems) {
            if (item.getText().trim().equalsIgnoreCase(category.trim())) {
                Actions actions = new Actions(driver);
                actions.moveToElement(item).pause(300).perform();
                String subCategorySelector = String.format("a[title='%s']", subCategory);
                try {
                    WebElement subCategoryLink = wait.until(
                            ExpectedConditions.elementToBeClickable(By.cssSelector(subCategorySelector)));
                    subCategoryLink.click();
                    logger.info("Hovered over category: {} and clicked on sub-category: {}", category, subCategory);
                    return new CategoryPage(driver);
                } catch (Exception e) {
                    logger.error("Sub-category '{}' not found under category '{}'", subCategory, category, e);
                    return null;
                }
            }
        }
        logger.warn("Main category '{}' not found in menu", category);
        return null;
    }
}