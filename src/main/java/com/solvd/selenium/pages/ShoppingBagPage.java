package com.solvd.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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
        waitForPageLoad();
    }

    public boolean isFirstBagItemVisible() {
        return isElementVisible(firstBagItem);
    }

    public boolean isFirstBagItemPriceVisible() {
        return isElementVisible(firstBagItemPrice);
    }

    public boolean isFirstBagItemQuantityVisible() {
        return isElementVisible(firstBagItemQuantity);
    }

    public boolean isFirstBagItemRemoveButtonVisible() {
        return isElementVisible(firstBagItemRemoveButton);
    }

    public void clickFirstBagItemRemoveButton() {
        clickElement(firstBagItemRemoveButton);
    }

    public boolean isEmptyBagMessageVisible() {
        return isElementVisible(emptyBagMessage);
    }

    public String getEmptyBagMessageText() {
        return getElementText(emptyBagMessage);
    }

    public boolean isContinueShoppingVisible() {
        return isElementVisible(continueShoppingButton);
    }

    public void clickContinueShopping() {
        clickElement(continueShoppingButton);
    }

    public int getQuantity() {
        return Integer.parseInt(quantityCount.getAttribute("value"));
    }

    public void clickOnQuantityCountPlus() {
        clickElement(quantityCountPlus);
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
                logger.warn("Failed to parse total price: {}", priceText, e);
                return false;
            }
        });
    }
}
