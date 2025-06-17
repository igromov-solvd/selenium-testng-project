package com.solvd.selenium.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BasePage class provides common functionality for all page objects in the
 * Selenium framework.
 * It includes methods for interacting with web elements, waiting for
 * conditions, and logging.
 */
public abstract class BasePage {
    protected static final Duration EXPLICIT_WAIT = Duration.ofSeconds(15);
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, EXPLICIT_WAIT);
        PageFactory.initElements(driver, this);
    }

    /**
     * Waits for the page to be fully loaded (document.readyState === "complete").
     * Logs the result and throws an exception if the page fails to load in time.
     */
    protected void waitForPageLoad() {
        try {
            boolean loaded = wait.until(driver -> {
                if (driver instanceof JavascriptExecutor) {
                    Object state = ((JavascriptExecutor) driver).executeScript("return document.readyState");
                    return "complete".equals(state);
                }
                return false;
            });
            if (loaded) {
                logger.info("Page loaded successfully: {}", driver.getCurrentUrl());
            } else {
                logger.warn("Page did not reach 'complete' readyState: {}", driver.getCurrentUrl());
            }
        } catch (Exception e) {
            logger.error("Timeout waiting for page to load: {}", driver.getCurrentUrl(), e);
            throw new RuntimeException("Timeout waiting for page to load: " + driver.getCurrentUrl(), e);
        }
    }

    /**
     * Waits until the given WebElement's text changes to the expected value.
     *
     * @param element      WebElement to monitor
     * @param expectedText The text to wait for
     * @return true if the text matches within the wait time, false otherwise
     */
    public boolean waitForElementText(WebElement element, String expectedText) {
        try {
            boolean result = wait.until(ExpectedConditions.textToBePresentInElement(element, expectedText));
            if (result) {
                logger.info("Element text changed to expected value: '{}'", expectedText);
            } else {
                logger.warn("Element text did not change to expected value: '{}'", expectedText);
            }
            return result;
        } catch (Exception e) {
            logger.error("Timeout waiting for element text to be '{}': {}", expectedText, element.toString(), e);
            return false;
        }
    }

    /**
     * Retrieves the current page title.
     *
     * @return the page title as a String, or an empty string if retrieval fails
     */
    public String getPageTitle() {
        try {
            return driver.getTitle();
        } catch (Exception e) {
            logger.warn("Failed to retrieve page title", e);
            return "";
        }
    }

    /**
     * Retrieves the current URL from the browser.
     *
     * @return the current URL as a String, or an empty string if retrieval fails
     */
    public String getCurrentUrl() {
        try {
            return driver.getCurrentUrl();
        } catch (Exception e) {
            logger.warn("Failed to retrieve current URL", e);
            return "";
        }
    }

    /**
     * Checks if an element is visible on the page
     * 
     * @param element WebElement to check
     * @return true if the element is visible, false otherwise
     */
    public boolean isElementVisible(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (Exception e) {
            logger.warn("Element not visible: {}", element.toString(), e);
            return false;
        }
    }

    /**
     * Checks if all elements are present in the DOM (may not be visible)
     * 
     * @param elements List of WebElements to check
     * @return true if all elements are present, false otherwise
     */
    public boolean areElementsPresent(List<WebElement> elements) {
        if (elements == null || elements.isEmpty()) {
            logger.warn("Empty or null elements list provided");
            return false;
        }

        boolean allPresent = true;
        for (WebElement element : elements) {
            try {
                // Just check presence, not visibility
                element.isEnabled();
            } catch (Exception e) {
                logger.warn("Element not present in DOM: {}", element.toString());
                allPresent = false;
            }
        }

        logger.debug("Presence check completed. All elements present: {}", allPresent);
        return allPresent;
    }

    /**
     * Checks if all elements are visible on the page
     * 
     * @param elements List of WebElements to check
     * @return true if all elements are visible, false otherwise
     */
    public boolean areElementsVisible(List<WebElement> elements) {
        if (elements == null || elements.isEmpty()) {
            logger.warn("Empty or null elements list provided");
            return false;
        }

        boolean allVisible = true;
        for (WebElement element : elements) {
            try {
                if (!wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed()) {
                    logger.warn("Element not displayed: {}", element.toString());
                    allVisible = false;
                }
            } catch (Exception e) {
                logger.warn("Element not visible: {}", element.toString(), e);
                allVisible = false;
            }
        }

        logger.info("Visibility check completed. All elements visible: {}", allVisible);
        return allVisible;
    }

    /**
     * Gets the visible text of an element
     * 
     * @param element WebElement to get text from
     * @return Text of the element or an empty string if not visible
     */
    public String getElementText(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).getText();
        } catch (Exception e) {
            logger.warn("Failed to get text from element: {}", element.toString(), e);
            return "";
        }
    }

    /**
     * Clicks on a WebElement after ensuring it is clickable
     * 
     * @param element WebElement to click
     */
    public void clickElement(WebElement element) {
        try {
            WebElement clickableElement = wait.until(ExpectedConditions.elementToBeClickable(element));
            clickableElement.click();
            logger.info("Successfully clicked element: {}", element.toString());
        } catch (Exception e) {
            logger.warn("Failed to click element: {}", element.toString(), e);
            throw new RuntimeException("Failed to click element: " + element.toString(), e);
        }
    }

    /**
     * Enters text into a WebElement after ensuring it is clickable
     * 
     * @param element WebElement to enter text into
     * @param text    Text to enter
     */
    public void enterText(WebElement element, String text) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.clear();
            element.sendKeys(text);
            logger.info("Entered text '{}' into element: {}", text, element.toString());
        } catch (Exception e) {
            logger.error("Failed to enter text '{}' into element: {}", text, element.toString(), e);
            throw new RuntimeException("Failed to enter text into element", e);
        }
    }
}