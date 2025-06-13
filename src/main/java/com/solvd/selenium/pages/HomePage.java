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

    public void enterSearchText(String searchText) {
        wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        searchInput.clear();
        searchInput.sendKeys(searchText);
        logger.info("Entered search text: {}", searchText);
    }

    public SearchResultsPage clickSearchButton() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        searchButton.click();
        logger.info("Clicked search button");
        return new SearchResultsPage(driver);
    }

    public SearchResultsPage searchForProduct(String searchText) {
        enterSearchText(searchText);
        return clickSearchButton();
    }

    public boolean isDeliveryTitleVisible() {
        return wait.until(ExpectedConditions.visibilityOf(deliveryTitle)).isDisplayed();
    }

    public String getDeliveryTitleText() {
        return wait.until(ExpectedConditions.visibilityOf(deliveryTitle)).getText();
    }

    public boolean isStoreLocatorVisible() {
        return wait.until(ExpectedConditions.visibilityOf(storeLocatorLink)).isDisplayed();
    }

    public boolean isHelpLinkVisible() {
        return wait.until(ExpectedConditions.visibilityOf(helpLink)).isDisplayed();
    }

    public boolean isLogoVisible() {
        return wait.until(ExpectedConditions.visibilityOf(logo)).isDisplayed();
    }

    public boolean isSearchBarVisible() {
        return wait.until(ExpectedConditions.visibilityOf(searchBar)).isDisplayed();
    }

    public boolean isAccountIconVisible() {
        return wait.until(ExpectedConditions.visibilityOf(accountIcon)).isDisplayed();
    }

    public boolean isFavoritesIconVisible() {
        return wait.until(ExpectedConditions.visibilityOf(favoritesIcon)).isDisplayed();
    }

    public boolean isShoppingBagIconVisible() {
        return wait.until(ExpectedConditions.visibilityOf(shoppingBagIcon)).isDisplayed();
    }

    public boolean isCheckoutButtonVisible() {
        return wait.until(ExpectedConditions.visibilityOf(checkoutButton)).isDisplayed();
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
            if (item.getText().equalsIgnoreCase(category)) {
                Object actions = new org.openqa.selenium.interactions.Actions(driver);
                ((Actions) actions).moveToElement(item).perform();
                String subCategorySelector = String.format("a[title='%s']", subCategory);
                WebElement subCategoryLink = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector(subCategorySelector)));
                subCategoryLink.click();
                logger.info("Hovered over category: {} and clicked on sub-category: {}", category, subCategory);
                // break;
                return new CategoryPage(driver);
            }
        }
        return null;
    }
}