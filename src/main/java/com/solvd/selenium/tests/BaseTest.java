package com.solvd.selenium.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BaseTest {

    private static final Duration IMPLICIT_WAIT = Duration.ofSeconds(10);
    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(30);

    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected static final String BASE_URL = "https://www.next.co.uk/";
    private static final String SELENIUM_HUB_URL = "http://localhost:4444";

    private boolean useRemoteDriver = false;

    protected WebDriver getDriver() {
        return driver.get();
    }

    @BeforeMethod
    @Parameters({ "browser", "remote" })
    public void setUp(String browser, String remote) {
        logger.info("Setting up WebDriver - Browser: {}, Remote: {}", browser, remote);

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
     * Setup local WebDriver (Chrome only)
     */
    private void setupLocalDriver(String browser) {
        if (!"chrome".equalsIgnoreCase(browser)) {
            throw new IllegalArgumentException("Only Chrome browser is supported: " + browser);
        }

        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = getChromeOptions();
        driver.set(new ChromeDriver(chromeOptions));
    }

    /**
     * Setup remote WebDriver through Selenium Server (Chrome only)
     */
    private void setupRemoteDriver(String browser) {
        try {
            if (!"chrome".equalsIgnoreCase(browser)) {
                throw new IllegalArgumentException("Only Chrome browser is supported: " + browser);
            }

            URL hubUrl = new URL(SELENIUM_HUB_URL);
            ChromeOptions chromeOptions = getChromeOptions();
            driver.set(new RemoteWebDriver(hubUrl, chromeOptions));

            logger.info("Connected to Selenium Hub at: {}", SELENIUM_HUB_URL);

        } catch (MalformedURLException e) {
            logger.error("Invalid Selenium Hub URL: {}", SELENIUM_HUB_URL, e);
            throw new RuntimeException("Failed to setup remote driver", e);
        }
    }

    /**
     * Common Chrome settings
     */
    private ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        // Additional options for remote execution
        if (useRemoteDriver) {
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

        return options;
    }

    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            logger.info("Closing WebDriver - Session ID: {}",
                    ((RemoteWebDriver) getDriver()).getSessionId());
            getDriver().quit();
            driver.remove();
        }
    }

    protected void navigateToHomePage() {
        logger.info("Navigating to: {}", BASE_URL);
        getDriver().get(BASE_URL);
    }
}