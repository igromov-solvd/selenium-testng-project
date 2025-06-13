package com.solvd.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class CategoryPage extends BasePage {

    @FindBy(css = "div[data-testid='plp-product-title'] > div > h1")
    private WebElement pageTitle;

    @FindBy(css = "div[data-testid='plp-product-grid-item']")
    private List<WebElement> products;

    @FindBy(css = "div[data-testid='plp-desktop-sort-button']")
    private WebElement sortingContainer;

    @FindBy(css = "header[data-testid='plp-filters-component-desktop']")
    private WebElement filtersContainer;

    public CategoryPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOf(pageTitle)).getText();
    }

    public boolean areProductsVisible() {
        return !products.isEmpty() && products.get(0).isDisplayed();
    }

    public boolean areFiltersVisible() {
        return wait.until(ExpectedConditions.visibilityOf(filtersContainer)).isDisplayed();
    }

    public boolean isSortingVisible() {
        return wait.until(ExpectedConditions.visibilityOf(sortingContainer)).isDisplayed();
    }

    public ProductPage clickFirstProduct() {
        wait.until(ExpectedConditions.elementToBeClickable(products.get(0))).click();
        return new ProductPage(driver);
    }
}