package com.solvd.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
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

    @FindBy(css = "div[data-testid='size-select']")
    private WebElement sizeSelector;

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
    }

    public boolean isProductNameVisible() {
        return wait.until(ExpectedConditions.visibilityOf(productName)).isDisplayed();
    }

    public boolean isProductPriceVisible() {
        return wait.until(ExpectedConditions.visibilityOf(productPrice)).isDisplayed();
    }

    public boolean isProductDescriptionVisible() {
        return wait.until(ExpectedConditions.visibilityOf(productDescription)).isDisplayed();
    }

    public boolean areProductImagesVisible() {
        return !productImages.isEmpty() && productImages.get(0).isDisplayed();
    }

    public boolean isSizeSelectionVisible() {
        return wait.until(ExpectedConditions.visibilityOf(sizeSelector)).isDisplayed();
    }

    public boolean isColorSelectionVisible() {
        return wait.until(ExpectedConditions.visibilityOf(colorSelector)).isDisplayed();
    }

    public boolean isAddToBagButtonVisible() {
        return wait.until(ExpectedConditions.visibilityOf(addToBagButton)).isDisplayed();
    }

    public boolean isFavouriteButtonVisible() {
        return wait.until(ExpectedConditions.visibilityOf(favouriteButton)).isDisplayed();
    }

    public boolean hasReviewsSection() {
        try {
            return reviewsSection.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectFirstAvailableSize() {
        wait.until(ExpectedConditions.elementToBeClickable(sizeSelector)).click();
        By firstAvailableSize = By
                .xpath("//ul[@aria-labelledby='size-input-label']/li[not(contains(., 'unavailable'))][1]");
        WebElement firstSize = wait.until(ExpectedConditions.elementToBeClickable(
                firstAvailableSize));
        firstSize.click();
        By sizeDropdown = By.cssSelector("ul[aria-labelledby='size-input-label']");
        // Wait for the size dropdown to disappear
        wait.until(ExpectedConditions.invisibilityOfElementLocated(sizeDropdown));
        // Wait condition for the dropdown also not present in DOM
        wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfAllElementsLocatedBy(
                sizeDropdown)));
    }

    public void clickAddToBag() {
        wait.until(ExpectedConditions.elementToBeClickable(addToBagButton)).click();
    }

    public boolean isAddToBagConfirmationVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(addToBagConfirmation)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public ShoppingBagPage clickViewBagButton() {
        wait.until(ExpectedConditions.elementToBeClickable(viewBagButton)).click();
        return new ShoppingBagPage(driver);
    }

    public boolean isCheckoutButtonVisible() {
        return wait.until(ExpectedConditions.visibilityOf(checkoutButton)).isDisplayed();
    }

    public void clickCheckoutButton() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
    }
}