package com.solvd.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class ProductPage extends BasePage {

    @FindBy(css = "h1[data-testid='product-title']")
    private WebElement productName;

    @FindBy(css = "div[data-testid='product-now-price'] > span")
    private WebElement productPrice;

    @FindBy(css = "p[data-testid='item-description']")
    private WebElement productDescription;

    @FindBy(css = "button[data-testid^='pdp-thumb']")
    private List<WebElement> productImages;

    @FindBy(css = "[data-testid='item-form-size-control']")
    private WebElement sizeSelector;

    @FindBy(css = "[data-testid='size-chips-button-group'] > button:not(.unavailable):first-of-type")
    private WebElement firstAvailableSize;

    @FindBy(css = "[data-testid='item-form-stock-status'] > p")
    private WebElement inStock;

    @FindBy(css = "div[data-testid='colour-chips-button-group']")
    private WebElement colorSelector;

    @FindBy(css = "button[data-testid='item-form-addToBag-button']")
    private WebElement addToBagButton;

    @FindBy(css = "button[data-testid='item-form-favourite-button']")
    private WebElement favouriteButton;

    @FindBy(css = "a[data-testid='item-title-review-stars']")
    private WebElement reviewsSection;

    @FindBy(css = "div[data-testid='header-mini-shopping-bag']")
    private WebElement addToBagConfirmation;

    @FindBy(css = "div[data-testid='header-mini-shopping-bag'] > div > div > a[href='https://www.next.co.uk/shoppingbag']")
    private WebElement viewBagButton;

    @FindBy(css = "div[data-testid='minibag-adaptive-checkout'] > a")
    private WebElement checkoutButton;

    public ProductPage(WebDriver driver) {
        super(driver);
        waitForPageLoad();
    }

    public boolean isProductNameVisible() {
        return isElementVisible(productName);
    }

    public boolean isProductPriceVisible() {
        return isElementVisible(productPrice);
    }

    public boolean isProductDescriptionVisible() {
        return isElementVisible(productDescription);
    }

    public boolean areProductImagesVisible() {
        return !productImages.isEmpty() && productImages.get(0).isDisplayed();
    }

    public boolean isSizeSelectionVisible() {
        return isElementVisible(sizeSelector);
    }

    public boolean isColorSelectionVisible() {
        return isElementVisible(colorSelector);
    }

    public boolean isAddToBagButtonVisible() {
        return isElementVisible(addToBagButton);
    }

    public boolean isFavouriteButtonVisible() {
        return isElementVisible(favouriteButton);
    }

    public boolean hasReviewsSection() {
        return isElementVisible(reviewsSection);
    }

    public void selectFirstAvailableSize() {
        clickElement(firstAvailableSize);
        waitForElementText(inStock, "In stock");
    }

    public void clickAddToBag() {
        clickElement(addToBagButton);
    }

    public boolean isAddToBagConfirmationVisible() {
        return isElementVisible(addToBagConfirmation);
    }

    public ShoppingBagPage clickViewBagButton() {
        clickElement(viewBagButton);
        return new ShoppingBagPage(driver);
    }

    public boolean isCheckoutButtonVisible() {
        return isElementVisible(checkoutButton);
    }

    public void clickCheckoutButton() {
        clickElement(checkoutButton);
    }
}