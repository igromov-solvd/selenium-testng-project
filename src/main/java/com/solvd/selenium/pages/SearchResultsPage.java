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

    public SearchResultsPage(WebDriver driver) {
        super(driver);
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
        try {
            wait.until(ExpectedConditions.visibilityOf(pageTitle));
            return pageTitle.getText();
        } catch (Exception e) {
            logger.warn("Could not get page title", e);
            return "";
        }
    }
}