package com.solvd.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class SearchResultsPage extends BasePage {

    private WebDriverWait wait;

    // Page Factory elements using CSS selectors
    @FindBy(css = "article[data-testid='plp-product-item']")
    private List<WebElement> productItems;

    @FindBy(css = "article[data-testid='plp-product-item'] h3")
    private List<WebElement> productTitles;

    @FindBy(css = "[data-testid='plp-no-products-message']")
    private WebElement noProductsMessage;

    @FindBy(css = "h1[data-testid='plp-page-title']")
    private WebElement pageTitle;

    public SearchResultsPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public boolean areProductsDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(productItems));
            boolean hasProducts = !productItems.isEmpty();
            logger.info("Products displayed: {}, Count: {}", hasProducts, productItems.size());
            return hasProducts;
        } catch (Exception e) {
            logger.error("No products found or page not loaded properly", e);
            return false;
        }
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
        System.out.println("\n=== PRODUCT SEARCH RESULTS ===");
        System.out.println("Total products found: " + titles.size());
        System.out.println("Product titles:");

        for (int i = 0; i < titles.size(); i++) {
            System.out.println((i + 1) + ". " + titles.get(i));
        }
        System.out.println("=================================\n");
    }

    public int getProductCount() {
        return productItems.size();
    }

    public String getPageTitleText() {
        try {
            wait.until(ExpectedConditions.visibilityOf(pageTitle));
            return pageTitle.getText();
        } catch (Exception e) {
            logger.warn("Could not get page title", e);
            return "";
        }
    }
}