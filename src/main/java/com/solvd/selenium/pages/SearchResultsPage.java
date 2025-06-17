package com.solvd.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;
import java.util.stream.Collectors;

public class SearchResultsPage extends BasePage {

    @FindBy(css = "p[data-testid='product_summary_title']")
    private List<WebElement> productTitles;

    @FindBy(css = "h1[data-testid='plp-results-title']")
    private WebElement pageTitle;

    @FindBy(css = "div[data-testid='plp-desktop-sort-button']")
    private WebElement sortingContainer;

    @FindBy(css = "header[data-testid='plp-filters-component-desktop']")
    private WebElement filtersContainer;

    @FindBy(css = "p[data-testid='product_summary_was_price'] > span")
    private List<WebElement> productPrices;

    @FindBy(css = "img[data-testid^='product_summary_image']")
    private List<WebElement> productImages;

    public SearchResultsPage(WebDriver driver) {
        super(driver);
        waitForPageLoad();
    }

    public List<String> getProductTitles() {
        wait.until(ExpectedConditions.visibilityOfAllElements(productTitles));
        List<String> titles = productTitles.stream()
                .map(WebElement::getText)
                .filter(text -> !text.trim().isEmpty())
                .collect(Collectors.toList());

        logger.info("Found {} product titles", titles.size());
        return titles;
    }

    public void printAllProductTitles() {
        List<String> titles = getProductTitles();
        logger.info("=== PRODUCT SEARCH RESULTS ===");
        logger.info("Total products found: {}", titles.size());
        logger.info("Product titles:");

        for (int i = 0; i < titles.size(); i++) {
            logger.info("{}. {}", (i + 1), titles.get(i));
        }
        logger.info("==============================");
    }

    public int getProductCount() {
        return getProductTitles().size();
    }

    public String getPageTitleText() {
        return getElementText(pageTitle);
    }

    public boolean areFiltersVisible() {
        return isElementVisible(filtersContainer);
    }

    public boolean isSortingVisible() {
        return isElementVisible(sortingContainer);
    }

    public boolean areProductPricesPresent() {
        return areElementsPresent(productPrices);
    }

    public boolean areProductImagesPresent() {
        return areElementsPresent(productImages);
    }
}