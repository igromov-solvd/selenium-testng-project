package com.solvd.selenium;

import com.solvd.selenium.config.ConfigManager;
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
import org.testng.annotations.BeforeSuite;
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

    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    protected final ConfigManager config = ConfigManager.getInstance();

    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private String browserName;
    private boolean useRemoteDriver = false;

    protected WebDriver getDriver() {
        return driver.get();
    }

    @BeforeSuite
    public void cleanScreenshotsFolder() {
        String screenshotDir = config.getScreenshotDirectory();
        File directory = new File(screenshotDir);

        if (directory.exists()) {
            try {
                FileUtils.cleanDirectory(directory);
                logger.info("Cleaned screenshots directory: {}", screenshotDir);
            } catch (IOException e) {
                logger.error("Failed to clean screenshots directory: {}", e.getMessage(), e);
            }
        } else {
            directory.mkdirs();
            logger.info("Created screenshots directory: {}", screenshotDir);
        }
    }

    @BeforeMethod
    @Parameters({ "browser", "remote" })
    public void setUp(@Optional String browser, @Optional String remote) {
        // Use TestNG parameters if provided, otherwise configuration is required
        if (browser == null && !config.hasProperty("default.browser")) {
            throw new RuntimeException(
                    "Browser must be specified either as TestNG parameter or 'default.browser' in config");
        }
        if (remote == null && !config.hasProperty("remote.execution.enabled")) {
            throw new RuntimeException(
                    "Remote execution must be specified either as TestNG parameter or 'remote.execution.enabled' in config");
        }

        String browserParam = browser != null ? browser : config.getDefaultBrowser();
        String remoteParam = remote != null ? remote : String.valueOf(config.isRemoteExecutionEnabled());

        logger.info("Setting up WebDriver - Browser: {}, Remote: {}", browserParam, remoteParam);

        this.browserName = browserParam.toLowerCase();
        this.useRemoteDriver = Boolean.parseBoolean(remoteParam);

        if (useRemoteDriver) {
            setupRemoteDriver(browserParam);
        } else {
            setupLocalDriver(browserParam);
        }

        // Common settings using configuration
        Duration implicitWait = Duration.ofSeconds(config.getImplicitWaitSeconds());
        Duration pageLoadTimeout = Duration.ofSeconds(config.getPageLoadTimeoutSeconds());

        getDriver().manage().timeouts().implicitlyWait(implicitWait);
        getDriver().manage().timeouts().pageLoadTimeout(pageLoadTimeout);
        getDriver().manage().window().maximize();

        logger.info("WebDriver setup completed - Mode: {}, Implicit Wait: {}s, Page Load Timeout: {}s",
                useRemoteDriver ? "Remote" : "Local", implicitWait.getSeconds(), pageLoadTimeout.getSeconds());
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
            String hubUrl = config.getSeleniumHubUrl();
            URL seleniumHubUrl = new URL(hubUrl);

            switch (browser.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = getChromeOptions();
                    driver.set(new RemoteWebDriver(seleniumHubUrl, chromeOptions));
                    break;

                case "firefox":
                    FirefoxOptions firefoxOptions = getFirefoxOptions();
                    driver.set(new RemoteWebDriver(seleniumHubUrl, firefoxOptions));
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported browser for remote execution: " + browser);
            }

            logger.info("Connected to Selenium Hub at: {}", hubUrl);

        } catch (MalformedURLException e) {
            String hubUrl = config.getSeleniumHubUrl();
            logger.error("Invalid Selenium Hub URL: {}", hubUrl, e);
            throw new RuntimeException("Failed to setup remote driver due to invalid Hub URL", e);
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
            int width = config.getWindowWidth();
            int height = config.getWindowHeight();
            options.addArguments(String.format("--window-size=%d,%d", width, height));
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
        options.addArguments("--disable-dev-shm-usage");

        if (useRemoteDriver) {
            int width = config.getWindowWidth();
            int height = config.getWindowHeight();
            options.addArguments(String.format("--width=%d", width));
            options.addArguments(String.format("--height=%d", height));
        }

        logger.debug("Firefox options configured");
        return options;
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        boolean screenshotOnFailure = config.isScreenshotOnFailureEnabled();

        if (result.getStatus() == ITestResult.FAILURE && screenshotOnFailure) {
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
        String baseUrl = config.getBaseUrl();
        logger.info("Navigating to: {}", baseUrl);
        getDriver().get(baseUrl);
    }

    private void captureScreenshot(String testName) {
        try {
            String screenshotDir = config.getScreenshotDirectory();

            // Create screenshots directory if it doesn't exist
            File directory = new File(screenshotDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate timestamp for filename
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = String.format("%s_%s_%s.png", testName, browserName, timestamp);

            // Take screenshot
            File screenshot = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            File destFile = new File(Paths.get(screenshotDir, fileName).toString());
            FileUtils.copyFile(screenshot, destFile);

            logger.info("Screenshot saved to: {}", destFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Failed to capture screenshot: {}", e.getMessage(), e);
        }
    }

    /**
     * Get the base URL from configuration
     */
    protected String getBaseUrl() {
        return config.getBaseUrl();
    }

    /**
     * Navigate to a specific URL relative to base URL
     */
    protected void navigateToPage(String relativePath) {
        String fullUrl = getBaseUrl() + relativePath;
        logger.info("Navigating to: {}", fullUrl);
        getDriver().get(fullUrl);
    }
}