package com.solvd.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class BaseTest {

    private static final Duration IMPLICIT_WAIT = Duration.ofSeconds(10);
    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(20);
    private static final String SCREENSHOT_DIR = "test-output/screenshots/";

    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected static final String BASE_URL = "https://www.next.co.uk/";
    private static final String SELENIUM_HUB_URL = "http://localhost:4444";

    private String browserName;
    private boolean useRemoteDriver = false;

    protected WebDriver getDriver() {
        return driver.get();
    }

    @BeforeMethod
    @Parameters({ "browser", "remote" })
    public void setUp(@Optional("firefox") String browser, @Optional("false") String remote) {
        logger.info("Setting up WebDriver - Browser: {}, Remote: {}", browser, remote);

        this.browserName = browser.toLowerCase();
        this.useRemoteDriver = Boolean.parseBoolean(remote);

        if (useRemoteDriver) {
            setupRemoteDriver(browser);
        } else {
            setupLocalDriver(browser);
        }

        // Common settings
        getDriver().manage().timeouts().implicitlyWait(IMPLICIT_WAIT);
        getDriver().manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT);
        getDriver().manage().window().maximize();

        logger.info("WebDriver setup completed - Mode: {}", useRemoteDriver ? "Remote" : "Local");
    }

    /**
     * Setup local WebDriver
     */
    private void setupLocalDriver(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = getChromeOptions();
                driver.set(new ChromeDriver(chromeOptions));
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = getFirefoxOptions();
                driver.set(new FirefoxDriver(firefoxOptions));
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    /**
     * Setup remote WebDriver
     */
    private void setupRemoteDriver(String browser) {
        try {
            URL hubUrl = new URL(SELENIUM_HUB_URL);

            switch (browser.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = getChromeOptions();
                    driver.set(new RemoteWebDriver(hubUrl, chromeOptions));
                    break;

                case "firefox":
                    FirefoxOptions firefoxOptions = getFirefoxOptions();
                    driver.set(new RemoteWebDriver(hubUrl, firefoxOptions));
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported browser for remote execution: " + browser);
            }

            logger.info("Connected to Selenium Hub at: {}", SELENIUM_HUB_URL);

        } catch (MalformedURLException e) {
            logger.error("Invalid Selenium Hub URL: {}", SELENIUM_HUB_URL, e);
            throw new RuntimeException("Failed to setup remote driver", e);
        }
    }

    /**
     * Chrome-specific options
     */
    private ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        if (useRemoteDriver) {
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

        return options;
    }

    /**
     * Firefox-specific options
     */
    private FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("dom.webdriver.enabled", false);
        options.addPreference("useAutomationExtension", false);
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage"); // Not typically needed for Firefox, but included for consistency

        if (useRemoteDriver) {
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
        }

        logger.info("Firefox options: {}", options);
        return options;
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            captureScreenshot(result.getName());
        }

        if (getDriver() != null) {
            try {
                if (getDriver() instanceof RemoteWebDriver) {
                    logger.info("Closing WebDriver - Session ID: {}", ((RemoteWebDriver) getDriver()).getSessionId());
                } else {
                    logger.info("Closing WebDriver (Local) - Browser: {}", browserName);
                }
                getDriver().quit();
            } catch (Exception e) {
                logger.error("Error while closing WebDriver for browser {}: {}", browserName, e.getMessage(), e);
            } finally {
                driver.remove();
            }
        }
    }

    protected void navigateToHomePage() {
        logger.info("Navigating to: {}", BASE_URL);
        getDriver().get(BASE_URL);
    }

    private void captureScreenshot(String testName) {
        try {
            // Create screenshots directory if it doesn't exist
            File directory = new File(SCREENSHOT_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate timestamp for filename
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = String.format("%s_%s_%s.png",
                    testName, browserName, timestamp);

            // Take screenshot
            File screenshot = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            File destFile = new File(Paths.get(SCREENSHOT_DIR, fileName).toString());
            FileUtils.copyFile(screenshot, destFile);

            logger.info("Screenshot saved to: {}", destFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Failed to capture screenshot: {}", e.getMessage(), e);
        }
    }
}