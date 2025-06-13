package com.solvd.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ShoppingBagPage extends BasePage {

    @FindBy(css = "div[id='items'] > div:first-child")
    private WebElement firstBagItem;

    @FindBy(css = "div[id='items'] > div:first-child span[class='sbm-item-price']")
    private WebElement firstBagItemPrice;

    @FindBy(css = "div[id='items'] > div:first-child div.qty-plusminus-control")
    private WebElement firstBagItemQuantity;

    @FindBy(css = "div[id='items'] > div:first-child a[class*='DeleteButton']")
    private WebElement firstBagItemRemoveButton;

    @FindBy(css = "div.sb-subtitle > span")
    private WebElement emptyBagMessage;

    @FindBy(css = "div.bag-empty > button")
    private WebElement continueShoppingButton;

    @FindBy(css = "div[id='items'] > div:first-child div.qty-plusminus-control > input")
    private WebElement quantityCount;

    @FindBy(css = "div[id='items'] > div:first-child div.qty-plusminus-control > button.qty-plus")
    private WebElement quantityCountPlus;

    @FindBy(css = "div.sbm-page-header div.sbm-order-total-price.sbm-summary-price-value")
    private WebElement totalPrice;

    public ShoppingBagPage(WebDriver driver) {
        super(driver);
    }

    public boolean isFirstBagItemVisible() {
        return wait.until(ExpectedConditions.visibilityOf(firstBagItem)).isDisplayed();
    }

    public boolean isFirstBagItemPriceVisible() {
        return wait.until(ExpectedConditions.visibilityOf(firstBagItemPrice)).isDisplayed();
    }

    public boolean isFirstBagItemQuantityVisible() {
        return wait.until(ExpectedConditions.visibilityOf(firstBagItemQuantity)).isDisplayed();
    }

    public boolean isFirstBagItemRemoveButtonVisible() {
        return wait.until(ExpectedConditions.visibilityOf(firstBagItemRemoveButton)).isDisplayed();
    }

    public void clickFirstBagItemRemoveButton() {
        wait.until(ExpectedConditions.elementToBeClickable(firstBagItemRemoveButton)).click();
    }

    public boolean isEmptyBagMessageVisible() {
        return wait.until(ExpectedConditions.visibilityOf(emptyBagMessage)).isDisplayed();
    }

    public String getEmptyBagMessageText() {
        return wait.until(ExpectedConditions.visibilityOf(emptyBagMessage)).getText();
    }

    public boolean isContinueShoppingVisible() {
        return wait.until(ExpectedConditions.visibilityOf(continueShoppingButton)).isDisplayed();
    }

    public void clickContinueShopping() {
        wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton)).click();
    }

    public int getQuantity() {
        return Integer.parseInt(quantityCount.getAttribute("value"));
    }

    public void clickOnQuantityCountPlus() {
        wait.until(ExpectedConditions.elementToBeClickable(quantityCountPlus)).click();
    }

    public double getProductPrice() {
        return Double.parseDouble(firstBagItemPrice.getText().replace("£", ""));
    }

    public void waitForTotalPriceChanged(Double expectedNewValue) {
        wait.until(driver -> {
            String priceText = totalPrice.getText().replace("£", "");
            try {
                double currentValue = Double.parseDouble(priceText);
                return Double.compare(currentValue, expectedNewValue) == 0;
            } catch (NumberFormatException e) {
                return false;
            }
        });
    }
}
