package com.solvd.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class HomePage extends BasePage {

    private WebDriverWait wait;

    @FindBy(css = "input#header-big-screen-search-box")
    private WebElement searchInput;

    @FindBy(css = "button[data-testid='header-search-bar-button']")
    private WebElement searchButton;

    @FindBy(css = "button#onetrust-accept-btn-handler")
    private WebElement acceptCookiesButton;

    public HomePage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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
        acceptCookiesIfPresent();
        enterSearchText(searchText);
        return clickSearchButton();
    }
}